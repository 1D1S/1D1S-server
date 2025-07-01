package com.odos.odos_server.domain.common.dto;

import lombok.Getter;

@Getter
public record S3Dto(String key, String presignedUrl) {}
