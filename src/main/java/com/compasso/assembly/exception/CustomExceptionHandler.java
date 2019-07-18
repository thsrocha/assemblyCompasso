package com.compasso.assembly.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{
    private final String NOT_FOUND = "INCORRECT_REQUEST";
    private final String BAD_REQUEST = "BAD_REQUEST";
    private final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
     
    @ExceptionHandler(RecordNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleAssemblyNotFoundException(RecordNotFoundException ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InternalServerErrorException.class)
    public final ResponseEntity<ErrorResponse> handleAssemblyInternalServerError(InternalServerErrorException ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(INTERNAL_SERVER_ERROR, details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> handleUserNotFoundException(BadRequestException ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
}
