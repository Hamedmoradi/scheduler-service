package com.scheduler.service.job;

import com.scheduler.service.repository.SchedulerRepository;
import com.scheduler.service.util.SchedulerClientTokenManager;
import com.scheduler.service.domain.SchedulerJobInfo;
import com.scheduler.service.webClient.paymentorder.PaymentOrderClient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
@DisallowConcurrentExecution
@NoArgsConstructor
@AllArgsConstructor
public class PolCronJob extends QuartzJobBean {

  @Autowired
  private SchedulerRepository schedulerRepository;
  @Autowired
  private PaymentOrderClient paymentOrderClient;
  @Autowired
  private SchedulerClientTokenManager schedulerClientTokenManager;

  @Override
  protected void executeInternal(JobExecutionContext context) {
    SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(context.getTrigger().getJobKey().getName());
    paymentOrderClient.polInquiry(schedulerClientTokenManager.getClientToken(), schedulerJobInfo.getCommand());
  }
}
