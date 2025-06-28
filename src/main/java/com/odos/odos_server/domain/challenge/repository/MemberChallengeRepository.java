package com.odos.odos_server.domain.challenge.repository;

import com.odos.odos_server.domain.challenge.entity.MemberChallenge;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {
  Optional<MemberChallenge> findByMemberIdAndChallengeId(Long currentMemberId, Long challengeId);
}
