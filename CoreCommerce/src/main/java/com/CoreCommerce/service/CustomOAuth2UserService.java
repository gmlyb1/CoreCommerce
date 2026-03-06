package com.CoreCommerce.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{
	
	private final MemberRepository memberRepository;

	 @Override
	    public OAuth2User loadUser(OAuth2UserRequest userRequest)
	            throws OAuth2AuthenticationException {

	        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
	                new DefaultOAuth2UserService();

	        OAuth2User oAuth2User = delegate.loadUser(userRequest);

	        String registrationId = userRequest.getClientRegistration().getRegistrationId();
	        
	        
	        Map<String, Object> attributes = oAuth2User.getAttributes();

	        String socialId = String.valueOf(attributes.get("id"));
	        String email = null;

	       System.out.println("registrationId:"+registrationId);
	       System.out.println("socialId:"+socialId);
	        if(registrationId.equals("kakao")) {

	            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

	            if(kakaoAccount != null) {
	                email = (String) kakaoAccount.get("email");
	            }
	        }

	        Member member = memberRepository
	                .findBySocialTypeAndSocialId(registrationId, socialId)
	                .orElse(null);

	        if(member == null) {

	        	 if(email == null) {
	 	        	email = registrationId + "_" + socialId + "@kakao.com";
	 	        }
	        	 
	        	String name = null;
	        	if(attributes.containsKey("properties")) {
	        		Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
	        		
	        		if(properties != null) {
	        			name = (String) properties.get("nickname");
	        		}
	        	}
	        	
	        	if(name == null) {
	        		name = "User_" + socialId.substring(0,5);
	        	}
	        	
	            Member newMember = new Member();
	            newMember.setEmail(email);
	            newMember.setName(name);
	            newMember.setPassword(new BCryptPasswordEncoder().encode("SOCIAL_LOGIN"));
	            newMember.setSocialType(registrationId);
	            newMember.setSocialId(socialId);
	            newMember.setRole("USER");

	            memberRepository.save(newMember);

	            member = newMember; // 🔥 바로 사용
	        }

	        // ✅ 반드시 return 추가
	        return new DefaultOAuth2User(
	                Collections.singleton(
	                        new SimpleGrantedAuthority(member.getRole())
	                ),
	                attributes,
	                "id"
	        );
	    }

}
