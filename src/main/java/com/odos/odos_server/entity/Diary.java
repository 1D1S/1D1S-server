package com.odos.odos_server.entity;

import com.odos.odos_server.Enum.Emotion;
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
  private Long diary_id;

  @Column private String diary_title;

  @Column private LocalDateTime diary_created_date;

  @Column private LocalDateTime diary_date; // 수행날짜?

  @Column
  @Enumerated(EnumType.STRING)
  private Emotion diary_feeling;

  @Column private Boolean diary_public;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column private Boolean diary_deleted;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<Challenge_Diary> challengeDiaries;

  @OneToMany(mappedBy = "diary", cascade = CascadeType.ALL)
  private List<Diary_Image> diaryImages;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "challenge_id")
  private Challenge challenge;
}
