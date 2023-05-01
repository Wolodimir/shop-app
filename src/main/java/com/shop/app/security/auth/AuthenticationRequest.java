package com.shop.app.security.auth;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public class AuthenticationRequest {
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
