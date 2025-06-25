package com.odos.odos_server.diary.entity;

import com.odos.odos_server.Enum.Feeling;
import com.odos.odos_server.challenge.entity.Challenge;
import com.odos.odos_server.member.entity.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String title;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @Column private LocalDateTime date;

  @Column
  @Enumerated(EnumType.STRING)
  private Feeling feeling;

  @Column private Boolean isPublic;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column private Boolean deleted;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryGoal> diaryGoals;

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
