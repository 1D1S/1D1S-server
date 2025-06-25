package com.odos.odos_server.global.error.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
  MEMBER_NOT_FOUND(404, "USER-001", "Member not found");

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
