package com.CoreCommerce.common;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
	                                    FilterChain filterChain) throws ServletException, IOException {

	        String authHeader = request.getHeader("Authorization");
	        String token = null;
	        String email = null;

	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            token = authHeader.substring(7);
	            if (jwtUtil.validateToken(token)) {
	                email = jwtUtil.getEmailFromToken(token);
	            }
	        }

	        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	            Member member = memberRepository.findByEmail(email).orElse(null);
	            if (member != null) {
	                UsernamePasswordAuthenticationToken authToken =
	                        new UsernamePasswordAuthenticationToken(member, null, null);
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        }

	        filterChain.doFilter(request, response);
	    }

}
