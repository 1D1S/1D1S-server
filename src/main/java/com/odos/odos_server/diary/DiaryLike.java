package com.odos.odos_server.diary;

import com.odos.odos_server.member.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "DiaryLike")
public class DiaryLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryLikeId;

  @ManyToOne
  @JoinColumn(name = "memberId")
  private Member member;

  @ManyToOne
  @JoinColumn(name = "diaryId")
  private Diary diary;
}
