package org.interonet.mercury.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo defaultErrorHandler(HttpServletRequest req, Exception ex) {
        String exceptionId = String.format("EXCEPTION:%s", UUID.randomUUID().toString());
        logger.error(exceptionId, ex);
        return new ErrorInfo(req.getRequestURL().toString(), exceptionId);
    }

    static class ErrorInfo {
        public final String url;
        public final String exceptionId;

        public ErrorInfo(String url, String exceptionId) {
            this.url = url;
            this.exceptionId = exceptionId;
        }
    }
}