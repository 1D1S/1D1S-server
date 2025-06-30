package com.odos.odos_server.domain.common;

import com.odos.odos_server.error.code.ErrorCode;
import com.odos.odos_server.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${spring.profiles.active:local}") // 현재 활성 프로필
    private String activeProfile;

    private final S3Client s3Client;

    public String uploadFile(MultipartFile file) {
        try {
            // String folderPrefix = resolveFolderByProfile(activeProfile); // dev/prod 결정
            // String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            // String key = folderPrefix + "/" + fileName;
            String key = UUID.randomUUID() + "-" + file.getOriginalFilename();

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return s3Client.utilities().getUrl(GetUrlRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build()).toExternalForm(); // 업로드된 파일의 URL 반환
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_UPLOAD_S3);
        }
    }

    /*
    private String resolveFolderByProfile(String profile) {
        return switch (profile) {
            case "dev" -> "dev";
            case "prod" -> "prod";
            default -> "local";
        };
    }
    */
}

