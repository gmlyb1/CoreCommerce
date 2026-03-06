package com.CoreCommerce.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

@Component
public class JwtFilter extends OncePerRequestFilter{

	 private final JwtUtil jwtUtil;
	 private final MemberRepository memberRepository;
	
	 public JwtFilter(JwtUtil jwtUtil, MemberRepository memberRepository) {
	        this.jwtUtil = jwtUtil;
	        this.memberRepository = memberRepository;
	    }

	 @Override
	 protected void doFilterInternal(HttpServletRequest request,
	                                 HttpServletResponse response,
	                                 FilterChain filterChain)
	         throws ServletException, IOException {

		 
	     String token = null;

	     // 🔥 쿠키에서 token 추출
	     if (request.getCookies() != null) {
	         for (Cookie cookie : request.getCookies()) {
	             if ("token".equals(cookie.getName())) {
	                 token = cookie.getValue();
	                 break;
	             }
	         }
	     }

	     if (token != null && jwtUtil.validateToken(token)) {

	         String email = jwtUtil.getEmailFromToken(token);
	         String role = jwtUtil.getRoleFromToken(token);
	         String socialId = jwtUtil.getSocialIdFromToken(token);

	         Member member = memberRepository.findBySocialId(socialId).orElse(null);
	         
	         if (member != null) {

	             UsernamePasswordAuthenticationToken authentication =
	                     new UsernamePasswordAuthenticationToken(
	                             member, // 🔥 principal 제대로
	                             null,
	                             Arrays.asList(new SimpleGrantedAuthority(role))
	                     );

	             SecurityContextHolder.getContext().setAuthentication(authentication);
	         }
	     }
	     
//	     System.out.println("FILTER AUTH AFTER SET = "  + SecurityContextHolder.getContext().getAuthentication());

	     filterChain.doFilter(request, response);
	 }

}
