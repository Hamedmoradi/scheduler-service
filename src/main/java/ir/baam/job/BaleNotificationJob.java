package ir.baam.job;

import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.REQUESTED;

import ir.baam.domain.SchedulerJobInfo;
import ir.baam.dto.SchedulerCommandDto;
import ir.baam.repository.SchedulerRepository;
import ir.baam.util.SchedulerClientTokenManager;
import ir.baam.webClient.standingOrder.StandingOrderClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
@NoArgsConstructor
@AllArgsConstructor
public class BaleNotificationJob extends QuartzJobBean {

  @Autowired
  private SchedulerRepository schedulerRepository;
  @Autowired
  private StandingOrderClient standingOrderClient;
  @Autowired
  private SchedulerClientTokenManager schedulerClientTokenManager;


  private static final String NOTIFICATION = "NOTIFICATION";


  @Override
  protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
    SchedulerJobInfo schedulerJobInfo =
        schedulerRepository.findByJobNameAndServiceType(context.getTrigger().getJobKey().getName(),
            REQUESTED.getValue());
    String status = null;
    switch (schedulerJobInfo.getCommand()) {
      case NOTIFICATION:
        status = REQUESTED.getValue();
        break;
      default:
        break;
    }
    SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, status);
    if (schedulerJobInfo.getCommand().equals(NOTIFICATION)) {
      log.info("send a  request to notification service with " + schedulerJobInfo.getCommand() + " command.");
      standingOrderClient.sendNotificationCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
      try {
        context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
      } catch (SchedulerException e) {
        e.printStackTrace();
      }
    }
    log.info("BaleNotificationJob End................");
  }

}
