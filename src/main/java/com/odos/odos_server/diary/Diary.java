package com.odos.odos_server.diary;

import com.odos.odos_server.Enum.Emotion;
import com.odos.odos_server.challenge.Challenge;
import com.odos.odos_server.challenge.ChallengeDiary;
import com.odos.odos_server.member.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryId;

  @Column private String diaryTitle;

  @Column private LocalDateTime diaryCreatedDate;

  @Column private LocalDateTime diaryDate; // 수행날짜?

  @Column
  @Enumerated(EnumType.STRING)
  private Emotion diaryFeeling;

  @Column private Boolean diaryPublic;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column private Boolean diaryDeleted;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<ChallengeDiary> challengeDiaries;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryImage> diaryImages;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challengeId")
  private Challenge challenge;

  @OneToMany(mappedBy = "diaryReport", cascade = CascadeType.ALL)
  private List<DiaryReport> diaryReports;
}
