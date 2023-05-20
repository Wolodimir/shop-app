package com.shop.app.security.auth;

import com.shop.app.security.error.ResponseError;
import com.shop.app.security.exception.UserDuplicatedException;
import com.shop.app.security.exception.UserNotFoundException;
import com.shop.app.security.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/auth")
@CrossOrigin(allowCredentials = "true", origins = "http://127.0.0.1:5173/")
public class AuthenticationController {

    private final AuthenticationService service;

    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail()) ) {
            throw new UserDuplicatedException(HttpStatusCode.valueOf(404),"Пользователь с такой почтой уже существует.");
        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest request,
                                                                HttpServletResponse httpServletResponse) {
        if(!userRepository.existsByEmail(request.getEmail())) {
            throw new UserNotFoundException(HttpStatus.NOT_FOUND,"Не найдет пользователь с данным email");
        }
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
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleUserNotFoundException(
            UserNotFoundException exception
    ) {
        return new ResponseError("Такого пользователя не существует.",404);
    }

    @ExceptionHandler(UserDuplicatedException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseError handleUserDuplicatedException(
            UserDuplicatedException exception
    ) {
        return new ResponseError("Пользователь с такой почтой уже существует.",500);
    }

}
