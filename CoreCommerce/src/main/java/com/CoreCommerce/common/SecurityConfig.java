package com.CoreCommerce.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtFilter jwtFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	

    	  http
          .csrf().disable()

          .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()

//          .authorizeRequests()
//              // ⭐ 정적 리소스 전부 허용
//              .antMatchers(
//                  "/",
//                  "/login",
//                  "/register",
//                  "/api/auth/**",
//                  "/css/**",
//                  "/js/**",
//                  "/images/**",
//                  "/webjars/**"
//              ).permitAll()
//              .anyRequest().authenticated()
//          .and()

          .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    	
//    	http
//        // REST API이므로 CSRF 비활성화
//        .csrf().disable()
//        
//        // 세션 사용 안함 (JWT 기반)
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//        
//        // 접근 권한 설정
//        .authorizeHttpRequests()
//            // 회원가입/로그인은 인증 없이 접근 허용
//            .requestMatchers(new AntPathRequestMatcher("/api/auth/**")).permitAll()
//            .anyRequest().authenticated()
//        .and()
//        
//        // JWT 필터 추가
//        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .authorizeHttpRequests()
//                .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
//                .anyRequest().authenticated();
//
//        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
