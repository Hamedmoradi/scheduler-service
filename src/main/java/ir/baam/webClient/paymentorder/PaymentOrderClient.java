package ir.baam.webClient.paymentorder;

import ir.baam.webClient.paymentorder.config.PaymentOrderConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "PaymentOrder", url = "${feign.client.payment-order-url}", configuration = PaymentOrderConfig.class)
public interface PaymentOrderClient {

  @GetMapping(value = "/payment-order/payment-order-procedure/scheduler-pol-inquiry")
  void polInquiry(@RequestHeader("Authorization") String token, @RequestParam String command);
}
