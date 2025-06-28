package com.odos.odos_server.domain.challenge.repository;

import com.odos.odos_server.domain.challenge.entity.ChallengeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ChallengeLikeRepository extends JpaRepository<ChallengeLike, Long> {
    boolean existsByChallengeIdAndMemberId(Long challengeId, Long currentMemberId);

    Collection<ChallengeLike> findByChallengeId(Long challengeId);

    ChallengeLike findByChallengeIdAndMemberId(Long challengeId, Long currentMemberId);
}
