package ir.baam.webClient.standingOrder;

import ir.baam.dto.SchedulerCommandDto;

import ir.baam.webClient.standingOrder.config.StandingOrderConfig;
import ir.baam.webClient.standingOrder.dto.StandingOrderTransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "StandingOrder", url = "${feign.client.standing-order-url}", configuration = {StandingOrderConfig.class})
public interface StandingOrderClient {

  @PostMapping(value = "/standing-order/runningScheduler/payment/prepare/transactions")
  List<StandingOrderTransactionResponse> prepareStandingOrderForCacheRecords(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommand);

  @PostMapping(value = "/standing-order/runningScheduler/payment/initiate")
  List<StandingOrderTransactionResponse> initiateCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommand);

  @PostMapping(value = "/standing-order/runningScheduler/payment/execution")
  List<StandingOrderTransactionResponse> executeCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommandDto);

  @PostMapping(value = "/runningScheduler/payment/determine/nextPeriod/status")
  List<StandingOrderTransactionResponse> completedCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommandDto);


}