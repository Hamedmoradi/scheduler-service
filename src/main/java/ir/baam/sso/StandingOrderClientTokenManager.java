//package ir.baam.sso;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang.time.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.Map;
//
//@Component
//@Scope("singleton")
//@RequiredArgsConstructor
//public class StandingOrderClientTokenManager {
//
//  @Autowired
//  private final SsoClient ssoClient;
//  private static Date startDate;
//  private static String token;
//
//  @Value("money-transfer svc-mgmt-agg-acc-part-perc-info payment-order-joint-account-transfer")
//  private String scopes;
//  @Value("client_credentials")
//  private String grantType;
//  @Value("Basic am9pbnQtYWNjb3VudC1jbGllbnQ6ZEw2dkIyY0kxYlIyYkwyZkM1a0czaE04bUd2QjNlRDFmRDFmQjJnUDQ=")
//  private String authorization;
//
//  public String getClientToken() {
//
//    if(this.token == null) {
//      this.token = getSsoAuth().getAccess_token();
//      this.startDate = new Date();
//    }
//
//    if(new Date().after(DateUtils.addHours(this.startDate, 24))) {
//      this.token = getSsoAuth().getAccess_token();
//      this.startDate = new Date();
//    }
//    return "Bearer " + token;
//  }
//
//  private SsoDto getSsoAuth() {
//    try {
//      return ssoClient.getJointTransferSsoToken(authorization, Map.of("scope", scopes, "grant_type", grantType));
//    } catch (Exception exp) {
//      return new SsoDto();
//    }
//  }
//}
