package scheduler.service.webClient.sso;

import scheduler.service.webClient.sso.dto.SsoDto;
import feign.Headers;
import scheduler.service.webClient.sso.config.SsoClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.Map;

@FeignClient(value = "Sso", url = "${feign.client.sso-url}", configuration = {SsoClientConfig.class})
public interface SsoClient {

  @PostMapping(value = "/identity/oauth2/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED)
  @Headers("Content-Type: application/x-www-form-urlencoded")
  SsoDto getStandingOrderSsoToken(@RequestHeader("Authorization") String basicAuthentication, @RequestBody Map<String, ?> form);

  @GetMapping(value = "/identity-manager/users/tan")
  void sendSms(@RequestHeader("Authorization") String clientToken, @RequestParam(value = "msg", required = false) String msg);

  @PostMapping(value = "/identity-manager/users/tan")
  void verifySms(@RequestHeader("Authorization") String clientToken, @RequestBody String raw);

}