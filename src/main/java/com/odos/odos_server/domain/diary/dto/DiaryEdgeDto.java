package com.odos.odos_server.domain.diary.dto;

/** Edge for Diary connection. */
public record DiaryEdgeDto(DiaryResponseDto node, String cursor) {
  public static DiaryEdgeDto from(DiaryResponseDto node, String cursor) {
    return new DiaryEdgeDto(node, cursor);
  }
}
