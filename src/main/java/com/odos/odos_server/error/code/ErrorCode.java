package com.odos.odos_server.error.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
  MEMBER_NOT_FOUND(404, "USER-001", "Member not found"),
  UNAUTHORIZED(401, "AUTH-001", "Unauthorized access"),
  INVALID_AUTH_PRINCIPAL(400, "AUTH-002", "Invalid authentication principal"),
  FAILED_UPLOAD_S3(504, "COMMON-002", "Failed upload image file");

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
