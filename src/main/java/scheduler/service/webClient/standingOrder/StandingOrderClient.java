package scheduler.service.webClient.standingOrder;

import scheduler.service.webClient.standingOrder.config.StandingOrderConfig;
import scheduler.service.dto.SchedulerCommandDto;

import scheduler.service.webClient.standingOrder.dto.StandingOrderTransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(value = "StandingOrder", url = "${feign.client.standing-order-url}", configuration = {StandingOrderConfig.class})
public interface StandingOrderClient {

  @PostMapping(value = "/standing-order/runningScheduler/payment/prepare/transactions")
  List<StandingOrderTransactionResponse> prepareStandingOrderForCacheRecords(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommand);

  @PostMapping(value = "/standing-order/runningScheduler/payment/execution")
  List<StandingOrderTransactionResponse> executeCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommandDto);

  @PostMapping(value = "/standing-order/runningScheduler/payment/determine/nextPeriod/status")
  List<StandingOrderTransactionResponse> completedCommand(@RequestHeader("Authorization") String token, @RequestBody SchedulerCommandDto schedulerCommandDto);

  @PostMapping(value = "/standing-order/transaction/notification")
  void sendNotificationCommand(@RequestHeader("Authorization") String token,
      @RequestBody SchedulerCommandDto schedulerCommandDto);

  @PostMapping(path = "/standing-order/runningScheduler/payment/prepare/satna")
  List<StandingOrderTransactionResponse> prepareSatnaTransaction(@RequestHeader("Authorization") String token,
      @RequestBody SchedulerCommandDto message);


}