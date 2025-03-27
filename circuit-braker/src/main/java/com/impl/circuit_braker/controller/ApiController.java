package com.impl.circuit_braker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.impl.circuit_braker.service.ExternalApiService;

@RestController
public class ApiController {

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping("/call-api")
    public String callApi() {
        return externalApiService.callExternalApi();
    }
}