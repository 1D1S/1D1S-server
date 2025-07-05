package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.Enum.ReportType;
import com.odos.odos_server.domain.diary.entity.DiaryReport;

public record CreateDiaryReportInput(Long diaryId, String content, ReportType reportType) {
  public static CreateDiaryReportInput from(DiaryReport report) {
    return new CreateDiaryReportInput(
        report.getDiary().getId(), report.getContent(), report.getType());
  }
}
