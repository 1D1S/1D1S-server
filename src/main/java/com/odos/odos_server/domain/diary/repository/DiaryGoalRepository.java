package com.odos.odos_server.domain.diary.repository;

import com.odos.odos_server.domain.diary.entity.DiaryGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryGoalRepository extends JpaRepository<DiaryGoal, Long> {}
