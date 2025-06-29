package com.odos.odos_server.domain.challenge.repository;

import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeLikeRepository extends JpaRepository<ChallengeLike, Long> {
  boolean existsByChallengeIdAndMemberId(Long challengeId, Long currentMemberId);

  Collection<ChallengeLike> findByChallengeId(Long challengeId);

  Optional<ChallengeLike> findByChallengeIdAndMemberId(Long challengeId, Long currentMemberId);
}
