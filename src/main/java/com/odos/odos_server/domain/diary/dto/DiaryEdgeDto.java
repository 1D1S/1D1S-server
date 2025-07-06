package com.odos.odos_server.domain.diary.dto;

/** Edge for Diary connection. */
public record DiaryEdgeDto(DiaryDto node, String cursor) {
  public static DiaryEdgeDto from(DiaryDto node, String cursor) {
    return new DiaryEdgeDto(node, cursor);
  }
}
