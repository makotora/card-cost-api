package com.makotora.cardcostapi.controller.exceptionhandler;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.makotora.cardcostapi.controller.response.ExceptionResponse;
import com.makotora.cardcostapi.exception.APIException;
import com.makotora.cardcostapi.exception.AlreadyExistsAPIException;
import com.makotora.cardcostapi.exception.CommunicationAPIException;
import com.makotora.cardcostapi.exception.InvalidArgumentAPIException;
import com.makotora.cardcostapi.exception.NotFoundAPIException;
import com.makotora.cardcostapi.exception.WaitTimeoutAPIException;

@ControllerAdvice
public class GlobalResponseEntityExceptionHandler extends ResponseEntityExceptionHandler
{
    private final Logger log;

    public GlobalResponseEntityExceptionHandler()
    {
        log = LoggerFactory.getLogger(GlobalResponseEntityExceptionHandler.class);
    }

    @ExceptionHandler(value = {AlreadyExistsAPIException.class, InvalidArgumentAPIException.class})
    protected ResponseEntity<ExceptionResponse> handleAlreadyExists(APIException ex, WebRequest request)
    {
        return buildAPIExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundAPIException.class})
    protected ResponseEntity<ExceptionResponse> handleInvalidArgument(APIException ex, WebRequest request)
    {
        return buildAPIExceptionResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {CommunicationAPIException.class})
    protected ResponseEntity<ExceptionResponse> handleCommunicationException(
        APIException ex,
        WebRequest request)
    {
        return buildAPIExceptionResponse(ex, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(value = {WaitTimeoutAPIException.class})
    protected ResponseEntity<ExceptionResponse> handleTimeoutException(APIException ex, WebRequest request)
    {
        return buildAPIExceptionResponse(ex, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // The APIException should normally be handled by the most 'specific' handlers
    @ExceptionHandler(value = {APIException.class})
    protected ResponseEntity<ExceptionResponse> handleAPIException(APIException ex, WebRequest request)
    {
        return buildAPIExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<ExceptionResponse> buildAPIExceptionResponse(
        APIException ex,
        HttpStatus httpStatus)
    {
        log.error("Preparing APIException response", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
                                                                    ex.getErrorCode().getCode(),
                                                                    LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, httpStatus);
    }

    // Reaching this handler suggests that an unhandled exception (not an APIException) was thrown
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<ExceptionResponse> handleAnyException(Exception ex, WebRequest request)
    {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(),
                                                                    null,
                                                                    LocalDateTime.now());

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
