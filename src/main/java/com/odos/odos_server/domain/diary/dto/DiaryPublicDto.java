package com.odos.odos_server.domain.diary.dto;

public record DiaryPublicDto(Boolean isPublic) {

  public static DiaryPublicDto from(Boolean isPublic) {
    return new DiaryPublicDto(isPublic);
  }
}
