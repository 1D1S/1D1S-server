package com.odos.odos_server.error.code;

import lombok.Getter;

@Getter
public enum ErrorCode {
  MEMBER_NOT_FOUND(404, "USER-001", "Member not found"),
  DIARY_NOT_FOUND(404, "DIARY-001", "Diary not found"),
  DIARYLIKE_NOT_FOUND(404, "DIARY-002", "DiaryLike not found");

  private final int status;
  private final String code;
  private final String message;

  ErrorCode(int status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
