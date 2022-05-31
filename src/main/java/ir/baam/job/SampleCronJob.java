package ir.baam.job;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import ir.baam.webClient.standingOrder.dto.SchedulerCommandDto;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.enumeration.CommandEnumeration;
import ir.baam.enumeration.StandingOrderTransactionStatusEnum;
import ir.baam.repository.SchedulerRepository;
import ir.baam.util.SchedulerClientTokenManager;
import ir.baam.webClient.standingOrder.StandingOrderClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import lombok.extern.slf4j.Slf4j;

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
    private  SchedulerClientTokenManager schedulerClientTokenManager;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
        log.info("SampleCronJob Start................");
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.INITIATE.getValue())) {
            log.info("send a instruction request to standing-order service");
            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date().getTime(), CommandEnumeration.INITIATE.getValue(), null, StandingOrderTransactionStatusEnum.PENDING.value());
            standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(),schedulerCommand);
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.EXECUTE.getValue())) {
            log.info("send a execute request to standing-order service");

            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date().getTime(), CommandEnumeration.EXECUTE.getValue(), null, StandingOrderTransactionStatusEnum.PROCESSING.value());
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(),schedulerCommand);
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.RESCHEDULE_FOR_FAILED_INSTRUCTIONS.getValue())) {
            log.info("send a instruction request for initiation_failed to standing-order service");

            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date().getTime(), CommandEnumeration.INITIATE.getValue(), null, StandingOrderTransactionStatusEnum.INITIATION_FAILED.value());
            standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(),schedulerCommand);
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.RESCHEDULE_FOR_FAILED_TRANSACTIONS.getValue())) {
            log.info("send a execution request for execution_failed to standing-order service");

            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date().getTime(), CommandEnumeration.EXECUTE.getValue(), null, StandingOrderTransactionStatusEnum.EXECUTION_CONNECTION_FAILED.value());
            standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(),schedulerCommand);
        }
        log.info("SampleCronJob End................");
    }
}