package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String email;      // 로그인용 이메일

	    @Column(nullable = false)
	    private String password;   // BCrypt로 암호화 저장

	    @Column(nullable = false)
	    private String name;       // 회원 이름

	    @Column(nullable = false)
	    private String role = "USER";  // USER / ADMIN 등 권한

	    @Column(name = "created_at", nullable = false, updatable = false)
	    private LocalDateTime createdAt = LocalDateTime.now();
}
