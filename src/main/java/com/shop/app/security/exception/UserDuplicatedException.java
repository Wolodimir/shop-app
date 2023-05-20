package com.shop.app.security.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;


public class UserDuplicatedException extends ResponseStatusException {


    public UserDuplicatedException(HttpStatusCode status, String reason) {
        super(status, reason);
    }
}
