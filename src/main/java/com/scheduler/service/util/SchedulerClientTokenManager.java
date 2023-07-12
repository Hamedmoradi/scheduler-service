package com.scheduler.service.util;


import com.scheduler.service.webClient.sso.SsoClient;
import com.scheduler.service.webClient.sso.dto.SsoDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@Scope("singleton")
@RequiredArgsConstructor
public class SchedulerClientTokenManager {

  @Autowired
  private final SsoClient ssoClient;
  private static Date startDate;
  private static String token;

  @Value("${sso.scopes}")
  private String scopes;
  @Value("${sso.grant-type}")
  private String grantType;
  @Value("${sso.authorization}")
  private String authorization;

  public String getClientToken() {

    if(this.token == null) {
      this.token = getSsoAuth().getAccess_token();
      this.startDate = new Date();
    }

    if(new Date().after(DateUtils.addHours(this.startDate, 24))) {
      this.token = getSsoAuth().getAccess_token();
      this.startDate = new Date();
    }
    return "Bearer " + token;
  }

  private SsoDto getSsoAuth() {
    try {
      return ssoClient.getStandingOrderSsoToken(authorization, Map.of("scope", scopes, "grant_type", grantType));
    } catch (Exception exp) {
      return new SsoDto();
    }
  }
}
