package ir.baam.job;

import java.util.stream.IntStream;

import ir.baam.domain.SchedulerJobInfo;
import ir.baam.enumeration.JobServicesEnum;
import ir.baam.enumeration.JobStatusEnum;
import ir.baam.repository.SchedulerRepository;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@DisallowConcurrentExecution
public class SampleCronJob extends QuartzJobBean {
    @Autowired
    SchedulerRepository schedulerRepository;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
        if (schedulerJobInfo.getServiceType().equals("RECURRING")) {
            log.info("salaaaaaaaaaaaaaaam");
        } else {
            log.info("SampleCronJob Start................");
            IntStream.range(0, 10).forEach(i -> {
                log.info("Counting - {}", i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        log.info("SampleCronJob End................");
    }
}
