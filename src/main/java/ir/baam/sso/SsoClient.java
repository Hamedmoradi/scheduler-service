package ir.baam.sso;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.Map;

@FeignClient(value = "Sso", url = "185.135.30.10:9443", configuration = {SsoClientConfig.class})
public interface SsoClient {

  @PostMapping(value = "/identity/oauth2/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED)
  @Headers("Content-Type: application/x-www-form-urlencoded")
  SsoDto getJointTransferSsoToken(@RequestHeader("Authorization") String basicAuthentication, @RequestBody Map<String, ?> form);

  @GetMapping(value = "/identity-manager/users/tan")
  void sendSms(@RequestHeader("Authorization") String clientToken, @RequestParam(value = "msg", required = false) String msg);

  @PostMapping(value = "/identity-manager/users/tan")
  void verifySms(@RequestHeader("Authorization") String clientToken, @RequestBody String raw);

}