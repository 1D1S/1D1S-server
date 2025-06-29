package com.odos.odos_server.domain.diary.dto;

import com.odos.odos_server.domain.common.Enum.ReportType;
import com.odos.odos_server.domain.diary.entity.DiaryReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiaryReportDTO {
  private Long diaryId;
  private String content;
  private ReportType reportType;

  public static DiaryReportDTO from(DiaryReport report) {
    return new DiaryReportDTO(report.getDiary().getId(), report.getContent(), report.getType());
  }
}
