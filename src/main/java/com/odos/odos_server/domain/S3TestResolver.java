package com.odos.odos_server.domain;

import com.odos.odos_server.domain.common.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3TestResolver {

  private final S3Service s3Service;

  @MutationMapping
  public String generateUploadUrl(String fileName) {
    String presignedUrl = s3Service.generatePresignedUrl(fileName);
    return presignedUrl;
  }

  @QueryMapping
  public String getImageUrl(String fileName) {
    return s3Service.getImageUrl(fileName);
  }
}
