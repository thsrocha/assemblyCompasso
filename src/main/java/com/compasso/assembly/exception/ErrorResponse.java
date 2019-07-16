package com.compasso.assembly.exception;

import java.util.Collection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	public ErrorResponse(String message, Collection<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
  
    private String message;
    private Collection<String> details;
}
