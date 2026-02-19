package com.CoreCommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CoreCommerce.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

	 // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);
    
    Member save(Member member);
}
