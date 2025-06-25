package com.odos.odos_server.domain.challenge.repository;

import com.odos.odos_server.domain.challenge.entity.ChallengeGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeGoalRepository extends JpaRepository<ChallengeGoal, Long> {}
