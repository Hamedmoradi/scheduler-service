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
import org.springframework.http.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@DisallowConcurrentExecution
public class SampleCronJob extends QuartzJobBean {
    @Autowired
    SchedulerRepository schedulerRepository;

    RestTemplate restTemplate=new RestTemplate();


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
        log.info("SampleCronJob Start................");
        if (schedulerJobInfo.getServiceType().equals("RECURRING")) {
            log.info("send a request to standing-order service");
       apiCall("hello","http://localhost:9027/standing-order/execute/");
        } else {
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

    private void apiCall(String message, String url) {
        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(message, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

}
