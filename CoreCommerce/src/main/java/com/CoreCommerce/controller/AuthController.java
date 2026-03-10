package com.CoreCommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    public String login(@RequestBody Member member,
                        HttpSession session){

        // 1️⃣ 회원 조회
        Member dbMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다"));

        // =====================================================
        // 🔥 2️⃣ 계정 잠금 체크
        // =====================================================
        
        if (Boolean.TRUE.equals(dbMember.getAccountLocked())) {

            LocalDateTime lockedUntil = dbMember.getLockedUntil();

            if (lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now())) {

                long days = ChronoUnit.DAYS.between(
                        LocalDateTime.now(),
                        lockedUntil
                );

                return "LOCKED:" + days;
            }

            return "계정이 잠겨 있습니다";
        }

        // =====================================================
        // 🔥 3️⃣ 비밀번호 체크
        // =====================================================
        if (!passwordEncoder.matches(member.getPassword(),
                dbMember.getPassword())) {

            return "비밀번호가 틀립니다";
        }
        
        
        // =====================================================
        // 🔥 4️⃣ 로그인 성공
        // =====================================================
        String token = jwtUtil.generateToken(dbMember.getEmail(), dbMember.getRole(), dbMember.getSocialId());

        session.setAttribute("loginUser", dbMember);
        

        return "redirect:/";
    }
    
	 // ===============================
	 // 아이디 찾기
	 // ===============================
    @PostMapping("/find-id")
    public ResponseEntity<?> findId(@RequestBody Map<String,String> req){

        String name = req.get("name");
        String email = req.get("email");

        Member member = memberRepository.findByEmail(email)
                .orElse(null);

        if(member == null){
            return ResponseEntity.status(404)
                    .body("회원 정보 없음");
        }

        // 🔥 이름 검증 (보안용)
        if(!member.getName().equals(name)){
            return ResponseEntity.status(400)
                    .body("정보가 일치하지 않습니다");
        }

        Map<String,String> result = new HashMap<>();
        result.put("email", member.getEmail());

        return ResponseEntity.ok(result);
    }
	 
	// ===============================
	// 비밀번호 찾기 (임시 비번 발급)
	// ===============================
    @PostMapping("/find-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String,String> req){

        String email = req.get("email");

        Member member = memberRepository.findByEmail(email)
                .orElse(null);

        if(member == null){
            return ResponseEntity.status(404)
                    .body("회원 정보 없음");
        }

        // 🔥 임시 비밀번호 생성
        String tempPassword = "tmp" + UUID.randomUUID().toString().substring(0,6);

        // 🔥 암호화
        String encodedPassword = passwordEncoder.encode(tempPassword);

        // 🔥 DB 업데이트 (반드시 update 사용)
        memberRepository.updatePassword(member.getId(), encodedPassword);

        Map<String,String> result = new HashMap<>();
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
	        return ResponseEntity.status(401).body("로그인이 필요합니다.");
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
	        return ResponseEntity.status(401).body("로그인이 필요합니다.");
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
