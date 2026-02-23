package com.CoreCommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.CoreCommerce.common.JwtUtil;
import com.CoreCommerce.domain.Member;
import com.CoreCommerce.repository.MemberRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final String JWT_SECRET = "corecommerce-corecommerce-corecommerce-123456";
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // 생성자 주입
    public AuthController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    @Value("${upload.profile.path}")
//    private String profileUploadPath;
    
    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member member){
    	member.setPassword(passwordEncoder.encode(member.getPassword()));
	    member.setRole("USER");
	    
	    int result = memberRepository.save(member); // int 반환
	    if(result > 0) {
	        return ResponseEntity.ok(member); // 저장된 Member 객체 그대로 반환
	    } else {
	        return ResponseEntity.status(500).body("회원가입 실패");
	    }
    }

    @PostMapping("/login")
    public String login(@RequestBody Member member,HttpSession session){
        Member dbMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

        if(!passwordEncoder.matches(member.getPassword(), dbMember.getPassword())) {
            throw new RuntimeException("비밀번호가 틀립니다");
        }

        // JWT 토큰 생성
//        String token = Jwts.builder()
//                .setSubject(dbMember.getEmail())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60)) // 1시간
//                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
//                .compact();
        String token = jwtUtil.generateToken(dbMember.getEmail());

        session.setAttribute("loginUser", dbMember);
        return "redirect:/";
    }
    
	 // ===============================
	 // 아이디 찾기
	 // ===============================
	 @PostMapping("/find-id")
	 public ResponseEntity<?> findId(@RequestParam String email){
	
	     Member member = memberRepository.findByEmail(email)
	             .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));
	
	     return ResponseEntity.ok(member.getEmail() != null ? member.getEmail() : "아이디 없음");
	 }
	 
	// ===============================
	// 비밀번호 찾기 (임시 비번 발급)
	// ===============================
	@PostMapping("/find-password")
	public ResponseEntity<?> resetPassword(@RequestParam String email){

	    Member member = memberRepository.findByEmail(email)
	            .orElseThrow(() -> new RuntimeException("회원 없음"));

	    // 임시 비밀번호 생성
	    String tempPassword = "tmp" + System.currentTimeMillis()%100000;

	    // 암호화
	    member.setPassword(passwordEncoder.encode(tempPassword));

	    memberRepository.save(member);

	    // 🔥 실제 프로젝트에서는 여기서 이메일 발송

	    Map<String, String> result = new HashMap<>();
	    result.put("tempPassword", tempPassword);

	    return ResponseEntity.ok(result);
	}
	
	// ===============================
	// 프로필 조회
	// ===============================
	@GetMapping("/profile")
	public ResponseEntity<?> profile(HttpSession session){

	    Member loginUser = (Member) session.getAttribute("loginUser");
	    
	    if(loginUser == null){
	        return ResponseEntity.status(401).body("로그인 안됨");
	    }

	    return ResponseEntity.ok(loginUser);
	}
	
	// ===============================
	// 프로필 수정
	// ===============================
	@PostMapping("/profile/update")
	public ResponseEntity<?> updateProfile(
	        @RequestParam(value = "imageFile", required = false)
	        MultipartFile imageFile,

	        @RequestParam("name") String name,
	        @RequestParam("phone") String phone,
	        @RequestParam("address1") String address1,
	        @RequestParam("address2") String address2,

	        HttpSession session) throws IOException {

	    Member loginUser =
	            (Member) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return ResponseEntity.status(401).body("로그인 필요");
	    }

	    // ==============================
	    // 🔵 기본 정보 수정
	    // ==============================
	    loginUser.setName(name);
	    loginUser.setPhone(phone);
	    loginUser.setAddress1(address1);
	    loginUser.setAddress2(address2);

	    // ==============================
	    // 🔵 이미지 업로드
	    // ==============================
	    if (imageFile != null && !imageFile.isEmpty()) {

	        // 🔥 반드시 absolute path 사용 (Tomcat work 폴더 방지)
	    	String uploadDir = new File("src/main/resources/static/images/profile").getAbsolutePath();

	        File dir = new File(uploadDir);

	        // 🔥 폴더 없으면 생성
	        if (!dir.exists()) {
	            boolean created = dir.mkdirs();
	            if (!created) {
	                throw new RuntimeException("프로필 폴더 생성 실패");
	            }
	        }

	        // 🔥 파일명 중복 방지
	        String filename =
	                UUID.randomUUID() + "_" +
	                imageFile.getOriginalFilename();

	        Path filePath = Paths.get(uploadDir, filename);

	        // 🔥 파일 저장
	        imageFile.transferTo(filePath.toFile());

	        // 🔥 브라우저 접근 경로 (product 방식 유지)
	        String imageUrl = "/images/profile/" + filename;

	        loginUser.setProfileImage(imageUrl);
	    }

	    // ==============================
	    // 🔥 DB 업데이트 (update 쿼리 사용)
	    // ==============================
	    memberRepository.updateProfile(loginUser);

	    // 🔥 세션 갱신
	    session.setAttribute("loginUser", loginUser);

	    return ResponseEntity.ok("프로필 수정 완료");
	}
	
	@PostMapping("/profile/password")
	public ResponseEntity<?> changePassword(@RequestBody Map<String,String> req,
	                                       HttpSession session){

	    Member loginUser = (Member) session.getAttribute("loginUser");

	    if(loginUser == null){
	        return ResponseEntity.status(401).body("로그인 필요");
	    }

	    String newPassword = req.get("newPassword");

	    // 🔥 암호화
	    String encodedPassword = passwordEncoder.encode(newPassword);

	    // 🔥 DB 직접 update (필드만 변경)
	    memberRepository.updatePassword(loginUser.getId(), encodedPassword);

	    // 🔥 세션 갱신
	    loginUser.setPassword(encodedPassword);
	    session.setAttribute("loginUser", loginUser);

	    return ResponseEntity.ok("비밀번호 변경 완료");
	}
}
