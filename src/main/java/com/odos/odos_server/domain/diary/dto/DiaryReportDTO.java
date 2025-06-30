package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.Enum.ReportType;
import com.odos.odos_server.domain.diary.entity.DiaryReport;

public record DiaryReportDTO(Long diaryId, String content, ReportType reportType) {
  public static DiaryReportDTO from(DiaryReport report) {
    return new DiaryReportDTO(report.getDiary().getId(), report.getContent(), report.getType());
  }
}
