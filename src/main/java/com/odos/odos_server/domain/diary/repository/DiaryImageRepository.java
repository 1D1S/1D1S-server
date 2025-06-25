package com.odos.odos_server.domain.diary.repository;

import com.odos.odos_server.domain.diary.entity.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {}
