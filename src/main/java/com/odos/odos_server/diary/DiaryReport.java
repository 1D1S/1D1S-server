package com.odos.odos_server.diary;

import com.odos.odos_server.Enum.ReportType;
import com.odos.odos_server.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "DiaryReport")
public class DiaryReport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryReportId;

  @Column
  @Enumerated(EnumType.STRING)
  private ReportType diaryReportType;

  @Column private String diaryReportContent;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "diaryId")
  private Diary diary;
}
