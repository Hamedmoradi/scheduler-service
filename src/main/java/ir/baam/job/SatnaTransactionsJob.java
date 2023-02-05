package ir.baam.job;

import static ir.baam.enumeration.JobServicesEnum.FUTURE;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.PENDING;

import ir.baam.domain.SchedulerJobInfo;
import ir.baam.dto.SchedulerCommandDto;
import ir.baam.repository.SchedulerRepository;
import ir.baam.util.SchedulerClientTokenManager;
import ir.baam.webClient.standingOrder.StandingOrderClient;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Log4j2
public class SatnaTransactionsJob extends QuartzJobBean {

  @Autowired
  private SchedulerRepository schedulerRepository;
  @Autowired
  private StandingOrderClient standingOrderClient;
  @Autowired
  private SchedulerClientTokenManager schedulerClientTokenManager;

  private static final String EXECUTE = "EXECUTE";

  @Override
  protected void executeInternal(JobExecutionContext context) {
    SchedulerJobInfo schedulerJobInfo =
        schedulerRepository.findByJobNameAndServiceType(context.getTrigger().getJobKey().getName(),
            FUTURE.getValue());
    String status = null;
    switch (schedulerJobInfo.getCommand()) {
      case EXECUTE:
        status = PENDING.getValue();
        break;
      default:
        break;
    }
    SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, status);
    if (schedulerJobInfo.getCommand().equals(EXECUTE)) {
      log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
      standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
    }
  }
}