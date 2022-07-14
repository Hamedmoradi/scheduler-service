package ir.baam.job;


import ir.baam.domain.SchedulerJobInfo;
import ir.baam.dto.SchedulerCommandDto;
import ir.baam.enumeration.CommandEnumeration;
import ir.baam.repository.SchedulerRepository;
import ir.baam.util.SchedulerClientTokenManager;
import ir.baam.webClient.standingOrder.StandingOrderClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.PENDING;
import static ir.baam.enumeration.StandingOrderTransactionStatusEnum.PROCESSING;

@Slf4j
@DisallowConcurrentExecution
@NoArgsConstructor
@AllArgsConstructor
public class StandingOrderCornJob extends QuartzJobBean {
    @Autowired
    private SchedulerRepository schedulerRepository;
    @Autowired
    private StandingOrderClient standingOrderClient;
    @Autowired
    private SchedulerClientTokenManager schedulerClientTokenManager;

    private static final String RECURRING = "RECURRING";
    private static final String INITIATE_COMMAND = "INITIATE";
    private static final String EXECUTE_COMMAND = "EXECUTE";


    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
//        StandingOrderJobs(schedulerJobInfo);
//        StandingOrderJobs(schedulerJobInfo, INITIATE, "send a instruction request to standing-order service", PENDING, INITIATE_COMMAND);
//        StandingOrderJobs(schedulerJobInfo, EXECUTE, "send a execute request to standing-order service", PROCESSING, EXECUTE_COMMAND);
//        StandingOrderJobs(schedulerJobInfo, RESCHEDULE_FOR_FAILED_INSTRUCTIONS, "send a instruction request for initiation_failed to standing-order service", INITIATION_FAILED, INITIATE_COMMAND);
//        StandingOrderJobs(schedulerJobInfo, RESCHEDULE_FOR_FAILED_TRANSACTIONS, "send a execution request for execution_failed to standing-order service", EXECUTION_FAILED, "executeCommand");
        if (schedulerJobInfo.getServiceType().equals(RECURRING) && schedulerJobInfo.getCommand().equals(CommandEnumeration.valueOf(schedulerJobInfo.getCommand()).getValue())) {
            if (schedulerJobInfo.getCommand() == INITIATE_COMMAND) {
                log.info("send a instruction request to standing-order service");
                SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, PENDING.value());
                standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            } else if (schedulerJobInfo.getCommand() == EXECUTE_COMMAND) {
                log.info("send a execute request to standing-order service");
                SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(schedulerJobInfo.getCommand(), null, PROCESSING.value());
                standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            }
        }
    }
//    @Synchronized
//    private void StandingOrderJobs(SchedulerJobInfo schedulerJobInfo) {
//        if (schedulerJobInfo.getServiceType().equals(RECURRING) && schedulerJobInfo.getCommand().equals(CommandEnumeration.valueOf(schedulerJobInfo.getCommand()).getValue())) {
//            if (schedulerJobInfo.getCommand() == INITIATE_COMMAND) {
//                log.info("send a instruction request to standing-order service");
//                SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date(), schedulerJobInfo.getCommand(), null, PENDING.value());
//                standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
//            } else if (schedulerJobInfo.getCommand() == EXECUTE_COMMAND){
//                log.info("send a execute request to standing-order service");
//                SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date(), schedulerJobInfo.getCommand(), null, PROCESSING.value());
//                standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
//            }
//        }
//    }
}