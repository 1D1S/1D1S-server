package com.odos.odos_server.error.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
  MEMBER_NOT_FOUND(404, "USER-001", "Member not found"),
  CHALLENGE_NOT_FOUND(404, "CHALLENGE-001", "Challenge not found"),
  CHALLENGE_APPLICANT_NOT_FOUND(404, "CHALLENGE-002", "Challenge apllicant not found"),
  UNAUTHORIZED(401, "AUTH-001", "Unauthorized access"),
  INVALID_AUTH_PRINCIPAL(400, "AUTH-002", "Invalid authentication principal"),
  ALREADY_APPLIED(400, "CHALLENGE-003", "Already applied to the challenge"),
  NO_PERMISSION(403, "AUTH-003", "No permission to perform this action"),
  INVALID_REQUEST(400, "CHALLENGE-004", "Member is not requested"),
  INVALID_DATE_FORMAT(400, "COMMON-001", "Invalid date format. Please use yyyy-MM-dd"),
  CHALLENGE_LIKE_NOT_FOUND(404, "CHALLENGE-005", "No challenge like"),
  ALREADY_LIKED(400, "CHALLENGE-006", "You already liked this challenge");

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
