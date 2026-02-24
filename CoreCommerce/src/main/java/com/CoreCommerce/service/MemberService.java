package com.CoreCommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
	
    // 회원가입
    public int register(Member member) {
        return memberRepository.save(member);
    }

    // 이메일로 회원 조회
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    
    public void updatePassword(Long id, String encodedPassword) {
    	memberRepository.updatePassword(id,encodedPassword);
    }
    
    public void updateProfile(Member loginUser) {
    	memberRepository.updateProfile(loginUser);
    }
    
    public List<Member> findAll(Member member) {
    	return memberRepository.findAll(member);
    }
    
    public void changeRole(Long memberId, String role) {
    	memberRepository.changeRole(memberId, role);
    }
    
    public void lockAccount(Long id, boolean b, Object object) {
    	memberRepository.lockAccount(id, b, object);
    }
}
