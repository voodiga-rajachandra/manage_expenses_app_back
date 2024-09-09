/*package com.daurenassanbaev.firstpetproject.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.http.HttpRequest;

@Slf4j
@ControllerAdvice(basePackages = "com.daurenassanbaev.firstpetproject.controllers")
public class ControllerExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpRequest request) {
        log.error("Failed to return response", ex);
        return "error/error500";
    }
}*/
