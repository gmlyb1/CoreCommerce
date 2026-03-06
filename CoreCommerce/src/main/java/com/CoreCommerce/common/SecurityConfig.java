package com.CoreCommerce.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.CoreCommerce.handler.OAuth2LoginSuccessHandler;
import com.CoreCommerce.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    	
//
//    	  http
//          .csrf().disable()
//
//          .sessionManagement()
//              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//          .and()
//
////          .authorizeRequests()
////              // ⭐ 정적 리소스 전부 허용
////              .antMatchers(
////                  "/",
////                  "/login",
////                  "/register",
////                  "/api/auth/**",
////                  "/css/**",
////                  "/js/**",
////                  "/images/**",
////                  "/webjars/**"
////              ).permitAll()
////              .anyRequest().authenticated()
////          .and()
//
//          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//    	
//
//        return http.build();
//    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	http
        .csrf(csrf -> csrf.disable())

        .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
        )

        // 🔥 권한 설정 추가 (이게 핵심)
//        .authorizeHttpRequests(auth -> auth
//                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/login/oauth2/**")).permitAll()
//                .anyRequest().authenticated()
//        )
//        .authorizeHttpRequests(auth -> auth
//                .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/oauth2/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/login/oauth2/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
//                .anyRequest().authenticated()
//        )
//        .authorizeHttpRequests(auth -> auth
//                .anyRequest().authenticated()
//        )

        // 🔥 소셜 로그인
        .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(user ->
                        user.userService(customOAuth2UserService)
                )
                .successHandler(oAuth2LoginSuccessHandler)
        )

        // 🔥 JWT 필터
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
    }
}
