package com.CoreCommerce.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

@Service
public class MemberService {

	  private final MemberRepository memberRepository;

	    public MemberService(MemberRepository memberRepository) {
	        this.memberRepository = memberRepository;
	    }
	
	    // 회원가입
	    public Member register(Member member) {
	        return memberRepository.save(member);
	    }
	
	    // 이메일로 회원 조회
	    public Optional<Member> findByEmail(String email) {
	        return memberRepository.findByEmail(email);
	    }
}
