package ir.baam.job;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.*;

@Slf4j
@DisallowConcurrentExecution
@NoArgsConstructor
@AllArgsConstructor
public class StandingOrderJobs extends QuartzJobBean {
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private StandingOrderClient standingOrderClient;
    @Autowired
    private SchedulerClientTokenManager schedulerClientTokenManager;


    private static final String RECURRING = "RECURRING";
    private static final String INITIATE = "INITIATE";
    private static final String EXECUTE = "EXECUTE";
    private static final String INITIATION_FAILED = "INITIATION_FAILED";
    private static final String EXECUTION_FAILED = "EXECUTION_FAILED";
    private static final String COMPLETED_PERIOD = "COMPLETED_PERIOD";
    private static final String TERMINATE_PERIOD = "TERMINATE_PERIOD";

    @Override
    protected void executeInternal(JobExecutionContext context) {
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByCommandAndServiceType(context.getTrigger().getJobKey().getName(),RECURRING);
        String status = null;
        switch (schedulerJobInfo.getCommand()) {
            case INITIATE:
                status = REQUESTED.getValue();
                break;
            case EXECUTE:
                status = PENDING.getValue();
                break;
            case INITIATION_FAILED:
                status = PROPOSED.getValue();
                break;
            case EXECUTION_FAILED:
                status = SUSPENDED.getValue();
                break;
            case COMPLETED_PERIOD:
                status = FULL_FILLED.getValue();
                break;
            case TERMINATE_PERIOD:
                status = TERMINATED.getValue();
                break;
            default:
                break;
        }
        SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, status);
        if (schedulerJobInfo.getCommand().equals(INITIATE)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(EXECUTE)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(INITIATION_FAILED)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.initiateFailedCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(EXECUTION_FAILED)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.executeFailedCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(COMPLETED_PERIOD)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(TERMINATE_PERIOD)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        log.info("SampleCronJob End................");
    }

}