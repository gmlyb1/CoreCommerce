package com.CoreCommerce.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.CoreCommerce.domain.Member;

public interface MemberRepository{

	 // 이메일로 회원 조회
    Optional<Member> findByEmail(String email);
    
    int save(Member member);

    void updatePassword(@Param("id") Long id,
            @Param("password") String password);

	void updateProfile(Member loginUser);
}
