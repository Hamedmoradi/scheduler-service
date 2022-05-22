package ir.baam.job;

import java.time.Instant;
import java.util.Date;

import io.netty.resolver.DefaultAddressResolverGroup;
import ir.baam.domain.SchedulerCommand;
import ir.baam.domain.SchedulerJobInfo;
import ir.baam.enumeration.CommandEnumeration;
import ir.baam.enumeration.StandingOrderTransactionStatusEnum;
import ir.baam.repository.SchedulerRepository;
import ir.baam.util.SsoDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.time.DateUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.quartz.QuartzJobBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class SampleCronJob extends QuartzJobBean {
    @Autowired
    SchedulerRepository schedulerRepository;

    private static Date startDate;
    private static String token;

    private String scopes = "transaction";
    private String grantType = "client_credentials";
//  private String authorization = "Basic am9pbnQtYWNjb3VudC1jbGllbnQ6ZEw2dkIyY0kxYlIyYkwyZkM1a0czaE04bUd2QjNlRDFmRDFmQjJnUDQ=";
    private String authorization = "Basic a2V5OnNlY3JldA==";

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String triggerName = context.getTrigger().getJobKey().getName();
        SchedulerJobInfo schedulerJobInfo = schedulerRepository.findByJobName(triggerName);
        log.info("SampleCronJob Start................");
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.INITIATE.getValue())) {
            log.info("send a instruction request to standing-order service");
            SchedulerCommand schedulerCommand = new SchedulerCommand(Instant.now(), CommandEnumeration.INITIATE.getValue(), null, StandingOrderTransactionStatusEnum.PENDING.value());
            apiCall(schedulerCommand, "/standing-order/runningScheduler/payment/initiate");
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.EXECUTE.getValue())) {
            log.info("send a execute request to standing-order service");

            SchedulerCommand schedulerCommand = new SchedulerCommand(Instant.now(), CommandEnumeration.EXECUTE.getValue(), null, StandingOrderTransactionStatusEnum.PROCESSING.value());
            apiCall(schedulerCommand, "/standing-order/runningScheduler/payment/execution");
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.RESCHEDULE_FOR_FAILED_INSTRUCTIONS.getValue())) {
            log.info("send a instruction request for initiation_failed to standing-order service");

            SchedulerCommand schedulerCommand = new SchedulerCommand(Instant.now(), CommandEnumeration.INITIATE.getValue(), null, StandingOrderTransactionStatusEnum.INITIATION_FAILED.value());
            apiCall(schedulerCommand, "/standing-order/runningScheduler/payment/initiate");
        }
        if (schedulerJobInfo.getServiceType().equals("RECURRING") && schedulerJobInfo.getCommand().equals(CommandEnumeration.RESCHEDULE_FOR_FAILED_TRANSACTIONS.getValue())) {
            log.info("send a execution request for execution_failed to standing-order service");

            SchedulerCommand schedulerCommand = new SchedulerCommand(Instant.now(), CommandEnumeration.EXECUTE.getValue(), null, StandingOrderTransactionStatusEnum.FAILED.value());
            apiCall(schedulerCommand, "/standing-order/runningScheduler/payment/execution");
        }
        log.info("SampleCronJob End................");
    }


    //TODO if connection has been lost raise exception and reschedule


    private String apiCall(SchedulerCommand schedulerCommand, String uri) {
        String token = getClientToken();
        WebClient webClient = WebClient
                .builder()
                .baseUrl("http://192.168.53.58:9027/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE))).build();
        return webClient
                .post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(schedulerCommand), SchedulerCommand.class)
                .retrieve()
                .bodyToMono(String.class).blockOptional().get();

    }

    private Mono<SsoDto> getToken(String basicAuthentication, String scopes, String grantType) {

        WebClient webClient = WebClient
                .builder()
                .baseUrl("http://192.168.53.58:9080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE)))
                .build();
        return webClient
                .post()
                .uri("/identity/oauth2/auth/token")
                .header(HttpHeaders.AUTHORIZATION, basicAuthentication)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("x-forwarded-for", "192.168.53.65")
                .body(BodyInserters.fromFormData("scope", scopes)
                        .with("grant_type", grantType))
                .retrieve()
                .bodyToMono(SsoDto.class)
                .onErrorMap(e -> new RuntimeException("exception", e));

    }


    public String getClientToken() {

        if (this.token == null) {
            this.token = getSsoAuth().getAccess_token();
            this.startDate = new Date();
        }

        if (new Date().after(DateUtils.addHours(this.startDate, 24))) {
            this.token = getSsoAuth().getAccess_token();
            this.startDate = new Date();
        }
        return "Bearer " + token;
    }

    private SsoDto getSsoAuth() {
        try {
            return getToken(authorization, scopes, grantType).blockOptional().get();
        } catch (Exception exp) {
            return new SsoDto();
        }
    }
}