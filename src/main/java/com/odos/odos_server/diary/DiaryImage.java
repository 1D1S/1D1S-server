package com.odos.odos_server.diary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "DiaryImage")
public class DiaryImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diaryImageId;

  @Column private String diaryImageUrl;

  @ManyToOne
  @JoinColumn(name = "diaryId")
  private Diary diary;
}
