package com.odos.odos_server.domain.diary.entity;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import com.odos.odos_server.domain.common.Enum.Feeling;
import com.odos.odos_server.domain.member.entity.Member;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String title;

  @CreatedDate
  @Column(updatable = false)
  private LocalDate createdDate;

  @Column private LocalDate date;

  @Column
  @Enumerated(EnumType.STRING)
  private Feeling feeling;

  @Column private Boolean isPublic = true;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column private Boolean deleted;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryGoal> diaryGoals;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryImage> diaryImages;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryLike> diaryLikes;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challengeId")
  private Challenge challenge;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<DiaryReport> diaryReports;

  public void update(
      String title,
      String content,
      Feeling feeling,
      Boolean isPublic,
      LocalDate date,
      Challenge challenge) {
    this.title = title;
    this.content = content;
    this.feeling = feeling;
    this.isPublic = isPublic;
    this.date = date;
    this.challenge = challenge;
  }
}
