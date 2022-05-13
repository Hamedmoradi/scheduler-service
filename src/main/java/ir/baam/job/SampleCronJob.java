package ir.baam.job;

import java.util.Date;
import java.util.stream.IntStream;

import ir.baam.domain.SchedulerCommand;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.enumeration.CommandEnumeration;
import ir.baam.enumeration.RecurringTransactionStatusEnum;
import ir.baam.repository.SchedulerRepository;
import ir.bmi.identity.security.BmiOAuth2User;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

@Slf4j
@DisallowConcurrentExecution
public class SampleCronJob extends QuartzJobBean {
    @Autowired
    SchedulerRepository schedulerRepository;

    RestTemplate restTemplate=new RestTemplate();


    public static final String TOKEN="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJncmFudCI6IlBBU1NXT1JEIiwiaXNzIjoiaHR0cDovL2FwaS5ibWkuaXIvc2VjdXJpdHkiLCJhdWQiOiJrZXkiLCJleHAiOjE2NTI1NTYyMTQ0NzIsIm5iZiI6MTY1MjQ2OTgxNDQ3Miwicm9sZSI6ImludGVybmV0IGJhbmstY3VzdG9tZXIiLCJzZXJpYWwiOiJmMWQyNGRkYS05NThhLTMzZjAtYTcxZC0yOGMxYjFiMTc1MTAiLCJzc24iOiIzOTIwMTgzNTg0IiwiY2xpZW50X2lkIjoiMTIzIiwic2NvcGVzIjpbImFjY291bnQtc3VwZXIiLCJ0cmFuc2FjdGlvbiIsInNzby1tYW5hZ2VyLWN1c3RvbWVyIiwic3NvLW1hbmFnZXItZW5yb2xsbWVudCIsImN1c3RvbWVyLXN1cGVyIl19.iqBrXrhe92z47FLl3KB6DFV6cx7TbZSSjxKBNYIufa4";


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
        log.info("SampleCronJob Start................");
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.INITIATE.getValue())) {
            log.info("send a request to standing-order service");
            Date date = new Date();
            SchedulerCommand schedulerCommand = new SchedulerCommand(date,CommandEnumeration.INITIATE.getValue(),null, RecurringTransactionStatusEnum.PENDING.value());
            apiCall(schedulerCommand,"http://localhost:9027/standing-order/runningScheduler/payment/initiate");
        } else {
            IntStream.range(0, 10).forEach(i -> {
                log.info("Counting - {}", i);
                try {
                    Thread.sleep(1000);
                }  catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.EXECUTE.getValue())) {
            log.info("send a request to standing-order service");
            Date date = new Date();
            SchedulerCommand schedulerCommand = new SchedulerCommand(date,CommandEnumeration.EXECUTE.getValue(),null, RecurringTransactionStatusEnum.PROCESSING.value());
            apiCall(schedulerCommand,"http://localhost:9027/runningScheduler/payment/execution");
        } else {
            IntStream.range(0, 10).forEach(i -> {
                log.info("Counting - {}", i);
                try {
                    Thread.sleep(1000);
                }  catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        log.info("SampleCronJob End................");
    }


    //TODO if connection has been lost raise exception and reschedule


    private BmiOAuth2User extractMyoAuth() {
        return (BmiOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void apiCall(SchedulerCommand message, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

//        headers.setBearerAuth(extractMyoAuth().getAttribute("ACCESS_TOKEN_VALUE").toString());
        headers.setBearerAuth(TOKEN);
        HttpEntity<SchedulerCommand> request = new HttpEntity<SchedulerCommand>(message, headers);
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }
}
