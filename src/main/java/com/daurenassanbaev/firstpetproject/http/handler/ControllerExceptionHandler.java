package com.daurenassanbaev.firstpetproject.http.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Slf4j
@ControllerAdvice(basePackages = "com.daurenassanbaev.http.controllers")
public class ControllerExceptionHandler {
    @ExceptionHandler
    public String handleControllerException(Exception ex, HttpServletRequest request) {
        log.error("Failed to return response", ex);
        return "error/error500";
    }
}
