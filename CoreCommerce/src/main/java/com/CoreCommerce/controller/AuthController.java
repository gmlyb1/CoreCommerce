package com.CoreCommerce.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String JWT_SECRET = "corecommerce-corecommerce-corecommerce-123456";

    // 생성자 주입
    public AuthController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member member){
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRole("USER");
        Member saved = memberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Member member){
        Member dbMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

        if(!passwordEncoder.matches(member.getPassword(), dbMember.getPassword())) {
            throw new RuntimeException("비밀번호가 틀립니다");
        }

        // JWT 토큰 생성
        String token = Jwts.builder()
                .setSubject(dbMember.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60)) // 1시간
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("member", dbMember);

        return ResponseEntity.ok(response);
    }
}
