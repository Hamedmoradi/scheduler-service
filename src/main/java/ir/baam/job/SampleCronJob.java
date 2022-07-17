package ir.baam.job;


import ir.baam.domain.SchedulerJobInfo;
import ir.baam.dto.SchedulerCommandDto;
import ir.baam.enumeration.StandingOrderTransactionStatusEnum;
import ir.baam.repository.SchedulerRepository;
import ir.baam.service.SchedulerJobService;
import ir.baam.util.SchedulerClientTokenManager;
import ir.baam.webClient.standingOrder.StandingOrderClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.PENDING;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.PROCESSING;

@Slf4j
@DisallowConcurrentExecution
@NoArgsConstructor
@AllArgsConstructor
public class SampleCronJob extends QuartzJobBean {
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private StandingOrderClient standingOrderClient;
    @Autowired
    private SchedulerClientTokenManager schedulerClientTokenManager;

    @Autowired
    private SchedulerJobService schedulerJobService;

    private static final String RECURRING = "RECURRING";
    private static final String INITIATE = "INITIATE";
    private static final String EXECUTE = "EXECUTE";
    private static final String INITIATION_FAILED = "INITIATION_FAILED";
    private static final String EXECUTION_FAILED = "EXECUTION_FAILED";

    @Override
    protected void executeInternal(JobExecutionContext context) {
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(context.getTrigger().getJobKey().getName());
        StandingOrderTransactionStatusEnum status = null;
        switch (schedulerJobInfo.getCommand()) {
            case INITIATE:
                status = PENDING;
                break;
            case EXECUTE:
                status = PROCESSING;
                break;
            case INITIATION_FAILED:
                status = StandingOrderTransactionStatusEnum.INITIATION_FAILED;
                break;
            case EXECUTION_FAILED:
                status = StandingOrderTransactionStatusEnum.EXECUTION_FAILED;
                break;
        }
        SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, status.value());
        if (schedulerJobInfo.getCommand().equals(INITIATE) || schedulerJobInfo.getCommand().equals(INITIATION_FAILED)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(EXECUTE) || schedulerJobInfo.getCommand().equals(EXECUTION_FAILED)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        log.info("SampleCronJob End................");
    }
}