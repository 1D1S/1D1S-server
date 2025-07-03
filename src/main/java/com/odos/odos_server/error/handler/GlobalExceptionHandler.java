package com.odos.odos_server.error.handler;

import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.code.ErrorResponse;
import com.odos.odos_server.error.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse("500", "Internal Server Error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
