package com.odos.odos_server.domain.diary.repository;

import com.odos.odos_server.domain.diary.entity.DiaryReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryReportRepository extends JpaRepository<DiaryReport, Long> {}
