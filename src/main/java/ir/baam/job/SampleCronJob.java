package ir.baam.job;


import java.util.Date;

import ir.baam.service.SchedulerJobService;
import ir.baam.dto.SchedulerCommandDto;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.enumeration.CommandEnumeration;
import ir.baam.enumeration.StandingOrderTransactionStatusEnum;
import ir.baam.repository.SchedulerRepository;
import ir.baam.util.SchedulerClientTokenManager;
import ir.baam.webClient.standingOrder.StandingOrderClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private SchedulerClientTokenManager schedulerClientTokenManager;

    @Autowired
    private SchedulerJobService schedulerJobService;

    private static final String RECURRING = "RECURRING";


    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
        log.info("SampleCronJob Start................");
        if (schedulerJobInfo.getServiceType().equals(RECURRING) && schedulerJobInfo.getCommand().equals(CommandEnumeration.INITIATE.getValue())) {
            log.info("send a instruction request to standing-order service");
            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date(), CommandEnumeration.INITIATE.getValue(), null, StandingOrderTransactionStatusEnum.PENDING.value());
            ResponseEntity initiateResult = standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            if (initiateResult.getStatusCodeValue() == 200) {
                //reschedule for next time
                log.info("scheduler for initiate reschedule *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            } else {
                log.info("scheduler for initiate  shutdown and reschedule it for next session *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().shutdown();
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            }
        }
        if (schedulerJobInfo.getServiceType().equals(RECURRING) && schedulerJobInfo.getCommand().equals(CommandEnumeration.EXECUTE.getValue())) {
            log.info("send a execute request to standing-order service");

            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date(), CommandEnumeration.EXECUTE.getValue(), null, StandingOrderTransactionStatusEnum.PROCESSING.value());
            ResponseEntity result = standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            if (result.getStatusCodeValue() == 200) {
                //reschedule for next time
                log.info("scheduler for execution reschedule *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            } else {
                log.info("scheduler for execution shutdown and reschedule it for next session *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().shutdown();
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            }
        }
        if (schedulerJobInfo.getServiceType().equals(RECURRING) && schedulerJobInfo.getCommand().equals(CommandEnumeration.RESCHEDULE_FOR_FAILED_INSTRUCTIONS.getValue())) {
            log.info("send a instruction request for initiation_failed to standing-order service");

            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date(), CommandEnumeration.INITIATE.getValue(), null, StandingOrderTransactionStatusEnum.INITIATION_FAILED.value());
            ResponseEntity initiationFailedResult = standingOrderClient.initiateCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            if (initiationFailedResult.getStatusCodeValue() == 200) {
                //reschedule for next time
                log.info("scheduler for execution reschedule *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            } else {
                log.info("scheduler for execution shutdown *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().shutdown();
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            }
        }
        if (schedulerJobInfo.getServiceType().equals(RECURRING) && schedulerJobInfo.getCommand().equals(CommandEnumeration.RESCHEDULE_FOR_FAILED_TRANSACTIONS.getValue())) {
            log.info("send a execution request for execution_failed to standing-order service");

            SchedulerCommandDto schedulerCommand = new SchedulerCommandDto(new Date(), CommandEnumeration.EXECUTE.getValue(), null, StandingOrderTransactionStatusEnum.EXECUTION_FAILED.value());
            ResponseEntity failedExecution = standingOrderClient.executeCommand(schedulerClientTokenManager.getClientToken(), schedulerCommand);
            if (failedExecution.getStatusCodeValue() == 200) {
                //reschedule for next time
                log.info("scheduler for execution reschedule *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            } else {
                log.info("scheduler for execution shutdown *********************** " + context.getScheduler().getSchedulerInstanceId());
                context.getScheduler().shutdown();
                context.getScheduler().rescheduleJob(context.getTrigger().getKey(), context.getTrigger());
            }
        }
        log.info("SampleCronJob End................");
    }
}