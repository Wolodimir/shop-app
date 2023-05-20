package com.shop.app.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(allowCredentials = "true", origins = "http://127.0.0.1:5173/")
public class TestController {

    @GetMapping("/someinfo")
    public String getSomeString() {
        return "SUKA";
    }
}
