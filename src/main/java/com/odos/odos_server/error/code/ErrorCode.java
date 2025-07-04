package com.odos.odos_server.error.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
  UNAUTHORIZED(401, "AUTH-001", "Unauthorized access"),
  INVALID_AUTH_PRINCIPAL(400, "AUTH-002", "Invalid authentication principal"),
  NO_PERMISSION(403, "AUTH-003", "No permission to perform this action"),
  INVALID_EMAIL(400, "AUTH-002", "Email is missing or invalid"),
  OAUTH_USER_NOT_FOUND(404, "AUTH-003", "User not found from OAuth provider"),
  INVALID_SIGNUP_PROVIDER(400, "AUTH-004", "Unexpected signup provider"),
  EMAIL_USER_NOT_FOUND(404, "AUTH-005", "User not found with given email"),

  MEMBER_NOT_FOUND(404, "USER-001", "Member not found"),
  INVALID_NICKNAME_FORMAT(
      400, "USER-002", "Nickname must be Korean or English, max 8 chars, no special characters"),
  NICKNAME_CHANGE_TOO_SOON(400, "USER-003", "Nickname can be changed only once a month"),
  CATEGORY_EMPTY(400, "USER-004", "At least one interest category must be selected"),
  CATEGORY_TOO_MANY(400, "USER-005", "Up to 3 interest categories can be selected"),

  CHALLENGE_NOT_FOUND(404, "CHALLENGE-001", "Challenge not found"),
  CHALLENGE_APPLICANT_NOT_FOUND(404, "CHALLENGE-002", "Challenge apllicant not found"),
  ALREADY_APPLIED(400, "CHALLENGE-003", "Already applied to the challenge"),
  INVALID_REQUEST(400, "CHALLENGE-004", "Member is not requested"),
  CHALLENGE_LIKE_NOT_FOUND(404, "CHALLENGE-005", "No challenge like"),
  ALREADY_LIKED(400, "CHALLENGE-006", "You already liked this challenge"),

  INVALID_DATE_FORMAT(400, "COMMON-001", "Invalid date format. Please use yyyy-MM-dd"),
  INVALID_CURSOR(400, "COMMON-002", "Invalid cursor"),
  ;

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
