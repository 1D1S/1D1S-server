package com.odos.odos_server.member.repository;

import com.odos.odos_server.member.entity.MemberInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberInterestRepository extends JpaRepository<MemberInterest, Long> {}
