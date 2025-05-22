package com.odos.odos_server.entity;

import com.odos.odos_server.Enum.Report_Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Diary_Report {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diary_report_id;

  @Column
  @Enumerated(EnumType.STRING)
  private Report_Type diary_report_type;

  @Column
  @Enumerated(EnumType.STRING)
  private String diary_report_content;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "diary_id")
  private Diary diary;

  //    이거 필요없는거 같은데 erd에 있네여 ..
  //    @ManyToOne
  //    @JoinColumn(name="")
  //    private Member member2;

}
