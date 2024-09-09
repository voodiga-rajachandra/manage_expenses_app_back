package com.daurenassanbaev.firstpetproject.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.daurenassanbaev.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
