package com.CoreCommerce.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.CoreCommerce.common.JwtUtil;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler{
	
	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;
	
	 @Override
	    public void onAuthenticationSuccess(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        Authentication authentication
	                                        )
	            throws IOException {

	        // 🔥 OAuth 사용자 정보 가져오기
	        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

	        String socialId = oAuth2User.getName();
	        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId(); 
	        
	        Optional<Member> member = memberRepository.findBySocialTypeAndSocialId(registrationId, socialId);
	        
	        Member loginMember = member.orElse(null);

	        if(loginMember != null) {
	            HttpSession session = request.getSession();
	            session.setAttribute("loginUser", loginMember);
	        }
	        
	        // 🔥 이메일 가져오기 (없으면 기본값)
	        String email = oAuth2User.getAttribute("email");

	        if (email == null) {
	            email = "no-email";
	        }

	        // 🔥 role 가져오기 (CustomOAuth2UserService에서 넣어둔 authority)
	        String role = authentication.getAuthorities()
	                .iterator()
	                .next()
	                .getAuthority();
	        
//	        String socialId = oAuth2User.getName();

	        // 🔥 JWT 생성
	        String token = jwtUtil.generateToken(email,role,socialId);
	        
	        
	        // 🔥 쿠키 저장
	        Cookie cookie = new Cookie("token", token);
	        cookie.setHttpOnly(true);
	        cookie.setPath("/");
	        cookie.setMaxAge(60 * 60); // 1시간

	        response.addCookie(cookie);

	        // 🔥 로그인 후 메인 이동
	        response.sendRedirect("/");
	    }

	
	
}
