package ir.baam.job;

import static ir.baam.enumeration.JobServicesEnum.SANTA;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.FULL_FILLED;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.PENDING;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.REQUESTED;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.TERMINATED;

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
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


@Slf4j
@DisallowConcurrentExecution
@NoArgsConstructor
@AllArgsConstructor
public class SatnaTransactionsJob extends QuartzJobBean {

  @Autowired
  private SchedulerRepository schedulerRepository;
  @Autowired
  private StandingOrderClient standingOrderClient;
  @Autowired
  private SchedulerClientTokenManager schedulerClientTokenManager;

  private static final String PREPARE_SATNA = "PREPARE_SATNA";
  private static final String EXECUTE = "EXECUTE";
  private static final String EXECUTION_FAILED = "EXECUTION_FAILED";
  private static final String TERMINATE_IN_THIS_PERIOD = "TERMINATE_IN_THIS_PERIOD";
  private static final String COMPLETED_IN_THIS_PERIOD = "COMPLETED_IN_THIS_PERIOD";

  @Override
  protected void executeInternal(JobExecutionContext context) {
    SchedulerJobInfo schedulerJobInfo =
        schedulerRepository.findByJobNameAndServiceType(context.getTrigger().getJobKey().getName(), SANTA.getValue());
    String status = null;
    switch (schedulerJobInfo.getCommand()) {
      case PREPARE_SATNA:
        status = REQUESTED.getValue();
        break;
      case EXECUTE:
        status = PENDING.getValue();
        break;
      case COMPLETED_IN_THIS_PERIOD:
        status = FULL_FILLED.getValue();
        break;
      case EXECUTION_FAILED:
      case TERMINATE_IN_THIS_PERIOD:
        status = TERMINATED.getValue();
        break;
      default:
        break;
    }
    SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, status);
    if (schedulerJobInfo.getCommand().equals(PREPARE_SATNA)) {
      log.info("send a  request to satna service with " + schedulerJobInfo.getCommand() + " command.");
      standingOrderClient.prepareStandingOrderForCacheRecords(schedulerClientTokenManager.getClientToken(), schedulerCommand);
      try {
        context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
      } catch (SchedulerException e) {
        e.printStackTrace();
      }
    }
    if (schedulerJobInfo.getCommand().equals(EXECUTE) || schedulerJobInfo.getCommand().equals(EXECUTION_FAILED)) {
      log.info("send a  request to satna service with " + schedulerJobInfo.getCommand() + " command.");
      standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
    }
    if (schedulerJobInfo.getCommand().equals(COMPLETED_IN_THIS_PERIOD) || schedulerJobInfo.getCommand().equals(TERMINATE_IN_THIS_PERIOD)) {
      log.info("send a  request to satna service with " + schedulerJobInfo.getCommand() + " command.");
      standingOrderClient.completedCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
    }
    log.info("SampleCronJob End................");
  }

}