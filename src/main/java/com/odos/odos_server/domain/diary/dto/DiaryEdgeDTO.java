package com.odos.odos_server.domain.diary.dto;

/** Edge for Diary connection. */
public record DiaryEdgeDTO(DiaryResponseDTO node, String cursor) {
  public static DiaryEdgeDTO from(DiaryResponseDTO node, String cursor) {
    return new DiaryEdgeDTO(node, cursor);
  }
}
