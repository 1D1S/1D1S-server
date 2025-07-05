package com.odos.odos_server.domain.challenge.repository;

import com.odos.odos_server.domain.challenge.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
  Page<Challenge> findByTitleContaining(String keyword, Pageable pageable);

  Page<Challenge> findByTitleContainingAndIdLessThanOrderByIdDesc(
      String keyword, Long id, Pageable pageable);
}
