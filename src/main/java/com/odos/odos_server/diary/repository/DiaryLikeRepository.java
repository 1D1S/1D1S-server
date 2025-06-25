package com.odos.odos_server.diary.repository;

import com.odos.odos_server.diary.entity.DiaryLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryLikeRepository extends JpaRepository<DiaryLike, Long> {}
