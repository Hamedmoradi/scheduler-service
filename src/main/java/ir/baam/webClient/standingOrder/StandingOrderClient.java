package ir.baam.webClient.standingOrder;

import ir.baam.webClient.standingOrder.dto.SchedulerCommandDto;

import ir.baam.webClient.standingOrder.config.StandingOrderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "PaymentOrder", url = "${feign.client.standing-order-url}", configuration = {StandingOrderConfig.class})
public interface StandingOrderClient {

  @PostMapping(value = "/standing-order/runningScheduler/payment/initiate")
  String initiateCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommand);


  @PostMapping(value = "/standing-order/runningScheduler/payment/execution")
  String executeCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommandDto);


}