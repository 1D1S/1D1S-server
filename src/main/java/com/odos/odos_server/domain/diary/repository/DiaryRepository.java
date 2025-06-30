package com.odos.odos_server.domain.diary.repository;

import com.odos.odos_server.domain.diary.entity.Diary;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
  @Query("select d from Diary d where d.member.id=:memberId")
  List<Diary> findAllByMyId(@Param("memberId") Long memberId);

  @Query("SELECT d FROM Diary d WHERE d.isPublic = true ORDER BY d.createdDate DESC")
  List<Diary> findAllPublicDiaries();

  Page<Diary> findByIsPublicTrue(Pageable pageable);

  long countByIsPublicTrue();
}
