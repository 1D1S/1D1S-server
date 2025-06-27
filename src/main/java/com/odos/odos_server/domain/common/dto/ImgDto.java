package com.odos.odos_server.domain.common.dto;

import com.odos.odos_server.domain.challenge.entity.ChallengeImage;
import com.odos.odos_server.domain.diary.entity.DiaryImage;

public record ImgDto(String url) {
  public static ImgDto from(ChallengeImage image) {
    return new ImgDto(image.getUrl());
  }

  public static ImgDto from(DiaryImage image) {
    return new ImgDto(image.getUrl());
  }
}
