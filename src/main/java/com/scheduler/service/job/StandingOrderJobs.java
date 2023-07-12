package com.scheduler.service.job;


import com.scheduler.service.domain.SchedulerJobInfo;
import com.scheduler.service.enumeration.JobServicesEnum;
import com.scheduler.service.enumeration.StandingOrderTransactionStatusEnum;
import com.scheduler.service.repository.SchedulerRepository;
import com.scheduler.service.util.SchedulerClientTokenManager;
import com.scheduler.service.webClient.standingOrder.StandingOrderClient;
import com.scheduler.service.dto.SchedulerCommandDto;
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
public class StandingOrderJobs extends QuartzJobBean {
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private StandingOrderClient standingOrderClient;
    @Autowired
    private SchedulerClientTokenManager schedulerClientTokenManager;

    private static final String EXECUTE = "EXECUTE";
    private static final String EXECUTION_FAILED = "EXECUTION_FAILED";
    private static final String TERMINATE_IN_THIS_PERIOD = "TERMINATE_IN_THIS_PERIOD";
    private static final String COMPLETED_IN_THIS_PERIOD = "COMPLETED_IN_THIS_PERIOD";
    private static final String PREPARE_STANDING_ORDER_SERVICE = "PREPARE_STANDING_ORDER_SERVICE";

    @Override
    protected void executeInternal(JobExecutionContext context) {
        SchedulerJobInfo schedulerJobInfo =
                schedulerRepository.findByJobNameAndServiceType(context.getTrigger().getJobKey().getName(), JobServicesEnum.RECURRING.getValue());
        String status = null;
        switch (schedulerJobInfo.getCommand()) {
            case PREPARE_STANDING_ORDER_SERVICE:
                status = StandingOrderTransactionStatusEnum.REQUESTED.getValue();
                break;
            case EXECUTE:
                status = StandingOrderTransactionStatusEnum.PROPOSED.getValue();
                break;
            case COMPLETED_IN_THIS_PERIOD:
                status = StandingOrderTransactionStatusEnum.FULL_FILLED.getValue();
                break;
            case EXECUTION_FAILED:
            case TERMINATE_IN_THIS_PERIOD:
                status = StandingOrderTransactionStatusEnum.TERMINATED.getValue();
                break;
            default:
                break;
        }
        SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, status);
        if (schedulerJobInfo.getCommand().equals(PREPARE_STANDING_ORDER_SERVICE)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.prepareStandingOrderForCacheRecords(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            try {
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());//TODO
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        if (schedulerJobInfo.getCommand().equals(EXECUTE) || schedulerJobInfo.getCommand().equals(EXECUTION_FAILED)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        if (schedulerJobInfo.getCommand().equals(COMPLETED_IN_THIS_PERIOD) || schedulerJobInfo.getCommand().equals(TERMINATE_IN_THIS_PERIOD)) {
            log.info("send a  request to standing-order service with " + schedulerJobInfo.getCommand() + " command.");
            standingOrderClient.completedCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
        }
        log.info("SampleCronJob End................");
    }

}