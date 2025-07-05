package com.odos.odos_server.domain.diary.repository;

import com.odos.odos_server.domain.diary.entity.DiaryLike;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryLikeRepository extends JpaRepository<DiaryLike, Long> {

  @Query(
      "Select like from DiaryLike like where like.member.id=:memberId and like.diary.id=:diaryId")
  Optional<DiaryLike> findDiaryLikeByMemberIdAndDiaryId(
      @Param("memberId") Long memberId, @Param("diaryId") Long diaryId);

  @Query("Select likes from DiaryLike likes where likes.diary.id=:diaryId")
  List<DiaryLike> findDiaryLikesByDiaryId(@Param("diaryId") Long diaryId);

  int countByDiaryId(Long diaryId);
}
