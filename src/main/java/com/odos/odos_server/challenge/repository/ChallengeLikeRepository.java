package com.odos.odos_server.challenge.repository;

import com.odos.odos_server.challenge.entity.ChallengeLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeLikeRepository extends JpaRepository<ChallengeLike, Long> {}
