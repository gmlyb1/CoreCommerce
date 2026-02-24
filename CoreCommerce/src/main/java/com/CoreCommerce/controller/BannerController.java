package com.CoreCommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.CoreCommerce.domain.Banner;
import com.CoreCommerce.domain.Pagination;
import com.CoreCommerce.service.BannerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {

	private final BannerService bannerService;
	
	@Value("${upload.banner.path}")
    private String bannerUploadPath;
	
	  /* =========================
	    🔥 목록
	 ========================== */
	 @GetMapping("/list")
	 public String list(@RequestParam(defaultValue = "1") int page,Model model) {
		 
	 	int size = 10;

	    int totalCount = bannerService.countAll();

	    Pagination pagination = new Pagination(page, size, totalCount);

	    List<Banner> bannerList =
	            bannerService.findPaging(
	                    pagination.getOffset(),
	                    pagination.getSize()
	            );

	    model.addAttribute("bannerList", bannerList);
	    model.addAttribute("pagination", pagination);

	
	     return "admin/banner/list";
	 }
	
	
	 /* =========================
	    🔥 등록 폼
	 ========================== */
	 @GetMapping("/create")
	 public String createForm(Model model) {
	
	     model.addAttribute("banner", new Banner());
	
	     return "admin/banner/create";
	 }
	
	
	 /* =========================
	    🔥 저장 (파일 업로드 포함)
	 ========================== */
	 @PostMapping("/create")
	 public String create(Banner banner,
	                      @RequestParam(value = "file", required = false)
	                      MultipartFile file) throws IOException {
	
	     // =============================
	     // 🔵 이미지 업로드 처리
	     // =============================
	     if (file != null && !file.isEmpty()) {
	
	         String uploadDir = new File(bannerUploadPath).getAbsolutePath();
	
	         File dir = new File(uploadDir);
	
	         // 폴더 없으면 생성
	         if (!dir.exists()) {
	             dir.mkdirs();
	         }
	
	         // 🔥 파일명 중복 방지
	         String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	
	         Path filePath = Paths.get(uploadDir, filename);
	
	         file.transferTo(filePath.toFile());
	
	         // 🔥 DB 저장 경로
	         banner.setImageUrl("/images/banner/" + filename);
	     }
	
	     // 기본값
	     if (banner.getUseYn() == null) {
	         banner.setUseYn("Y");
	     }
	
	     bannerService.saveBanner(banner);
	
	     return "redirect:/admin/banner/list";
	 }
	
	
	 /* =========================
	    🔥 수정 폼
	 ========================== */
	 @GetMapping("/edit/{id}")
	 public String editForm(@PathVariable Long id, Model model) {
	
	     Banner banner = bannerService.getBannerById(id);
	
	     model.addAttribute("banner", banner);
	
	     return "admin/banner/edit";
	 }
	
	
	 /* =========================
	    🔥 수정 (파일 선택 시 교체)
	 ========================== */
	 @PostMapping("/edit")
	 public String update(Banner banner,
	                      @RequestParam(value = "file", required = false)
	                      MultipartFile file) throws IOException {
	
	     // 🔵 새 파일 업로드하면 교체
	     if (file != null && !file.isEmpty()) {
	
	         String uploadDir = new File(bannerUploadPath).getAbsolutePath();
	
	         File dir = new File(uploadDir);
	         if (!dir.exists()) {
	             dir.mkdirs();
	         }
	
	         String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
	         Path filePath = Paths.get(uploadDir, filename);
	
	         file.transferTo(filePath.toFile());
	
	         banner.setImageUrl("/images/banner/" + filename);
	     }
	
	     bannerService.updateBanner(banner);
	
	     return "redirect:/admin/banner/list";
	 }
	
	
	 /* =========================
	    🔥 삭제
	 ========================== */
	 @GetMapping("/delete/{id}")
	 public String delete(@PathVariable Long id) {
	
	     bannerService.deleteBanner(id);
	
	     return "redirect:/admin/banner/list";
	 }
	}
