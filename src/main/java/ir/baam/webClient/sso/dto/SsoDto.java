package ir.baam.webClient.sso.dto;

import lombok.Data;

@Data
public class SsoDto {

  private String last_logins;
  private String token_type;
  private Integer expires_in;
  private String access_token;
}