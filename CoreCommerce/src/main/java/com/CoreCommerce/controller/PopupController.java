package com.CoreCommerce.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.CoreCommerce.domain.Popup;
import com.CoreCommerce.service.PopupService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PopupController {

	 private final PopupService popupService;

	 @Value("${upload.popup.path}")
	 private String popupUploadPath;
	 
	    /* ==========================
	       🔹 관리자 - 목록
	    ========================== */
	    @GetMapping("/admin/popup/list")
	    public String list(Model model) {
	        model.addAttribute("popups", popupService.findAll());
	        return "admin/popup/list";
	    }

	    /* ==========================
	       🔹 관리자 - 등록폼
	    ========================== */
	    @GetMapping("/admin/popup/write")
	    public String writeForm(Model model) {
	        model.addAttribute("popup", new Popup());
	        return "admin/popup/write";
	    }

	    /* ==========================
	       🔹 관리자 - 저장
	    ========================== */
	    @PostMapping("/admin/popup/save")
	    public String save(Popup popup,
	                       @RequestParam(value = "file", required = false)
	                       MultipartFile file) throws IOException {

	        // ==========================
	        // 🔵 이미지 업로드
	        // ==========================
	        if (file != null && !file.isEmpty()) {

	            String uploadDir = new File(popupUploadPath).getAbsolutePath();

	            File dir = new File(uploadDir);

	            // 🔥 폴더 없으면 생성
	            if (!dir.exists()) {
	                boolean created = dir.mkdirs();
	                if (!created) {
	                    throw new RuntimeException("팝업 폴더 생성 실패");
	                }
	            }

	            // 🔥 파일명 중복 방지
	            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

	            Path filePath = Paths.get(uploadDir, filename);

	            // 🔥 파일 저장
	            file.transferTo(filePath.toFile());

	            // 🔥 브라우저 접근 경로
	            popup.setImageUrl("/images/popup/" + filename);
	        }

	        // ==========================
	        // 🔵 기본 값 처리
	        // ==========================
	        if (popup.getIsActive() == null) {
	            popup.setIsActive(false);
	        }

	        // ==========================
	        // 🔥 저장 / 수정 분기
	        // ==========================
	        if (popup.getId() != null) {
	            popupService.update(popup);
	        } else {
	            popupService.save(popup);
	        }

	        return "redirect:/admin/popup/list";
	    }

	    /* ==========================
	       🔹 관리자 - 수정폼
	    ========================== */
	    @GetMapping("/admin/popup/edit/{id}")
	    public String editForm(@PathVariable Long id, Model model) {
	        model.addAttribute("popup", popupService.findById(id));
	        return "admin/popup/write";
	    }

	    /* ==========================
	       🔹 관리자 - 삭제
	    ========================== */
	    @GetMapping("/admin/popup/delete/{id}")
	    public String delete(@PathVariable Long id) {
	        popupService.delete(id);
	        return "redirect:/admin/popup/list";
	    }
}
