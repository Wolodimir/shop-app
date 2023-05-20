package com.shop.app.security.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@CrossOrigin(allowCredentials = "true", origins = "http://localhost:5173/")
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request,
                                                                HttpServletResponse httpServletResponse) {
        AuthenticationResponse response = service.authenticate(request);
        /*Cookie cookie = new Cookie("Set-Cookie", response.getAccessToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(Duration.of(1, ChronoUnit.DAYS).toSecondsPart());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);*/
        String value = "jwt=" + response.getAccessToken() + ";Path=/;Domain=localhost;Max-Age=" +
                Duration.of(1, ChronoUnit.DAYS).getSeconds() + ";HttpOnly;Secure;SameSite=None";

        httpServletResponse.setHeader("Set-Cookie", value);
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "Authorization");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
        //httpServletResponse.addHeader(jwtConfig.getAuthorizationHeader(),jwtConfig.getTokenPrefix() + token);

        return ResponseEntity.ok(response);
    }
}
