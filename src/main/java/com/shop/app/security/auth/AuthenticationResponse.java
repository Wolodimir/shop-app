package com.shop.app.security.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class AuthenticationResponse {

  @JsonProperty("access_token")
  private String accessToken;

  public AuthenticationResponse() {
  }

  public AuthenticationResponse(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
