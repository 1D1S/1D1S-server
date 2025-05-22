package com.odos.odos_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Diary_Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long diary_image_id;

  @Column private String diary_image_url;

  @ManyToOne
  @JoinColumn(name = "diary_id")
  private Diary diary;
}
