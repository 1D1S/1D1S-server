package com.odos.odos_server.error.exception;

import com.odos.odos_server.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
  private final ErrorCode errorCode;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
