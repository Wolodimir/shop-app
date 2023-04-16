package com.mtorg.crm.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/someinfo")
    public String getSomeString() {
        return "SUKA";
    }
}
