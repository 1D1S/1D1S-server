package com.odos.odos_server.challenge.repository;

import com.odos.odos_server.challenge.entity.MemberChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {
}
