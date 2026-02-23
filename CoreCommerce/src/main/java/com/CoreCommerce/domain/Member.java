package com.CoreCommerce.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

	    private String phone;
	    private String zipcode;
	    private String address1;
	    private String address2;
	    private String profileImage;
	    
	    @CreatedDate
	    @Column(name = "created_at", updatable = false)
	    private LocalDateTime createdAt;

	    @LastModifiedDate
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;
}
