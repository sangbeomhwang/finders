package net.datasa.finders.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("portfolio")
public class PortfolioController {

	@GetMapping("edit")
    public String protfolio() {
		
        return "/portfolio/portfolio";
    }
	
	@PostMapping("save")
    public String save(@RequestParam("editorContent") String contents) {
		log.debug("출력내용 : {}",contents);
        return "redirect:/portfolio/portfolio";
    }
	
	// 이미지 업로드 경로 설정 (로컬 경로 예시)
	private static final String UPLOAD_DIR = "C:/upload/";

	/*
	 * @PostMapping("/upload-image") public ResponseEntity<Map<String, Object>>
	 * uploadImage(@RequestParam("upload") MultipartFile file) {
	 * log.debug("파일:{}",file); Map<String, Object> response = new HashMap<>(); try
	 * { // 고유한 파일 이름 생성 String originalFilename = file.getOriginalFilename();
	 * String fileExtension =
	 * originalFilename.substring(originalFilename.lastIndexOf(".")); String
	 * savedFilename = UUID.randomUUID().toString() + fileExtension;
	 * 
	 * // 파일 저장 File saveFile = new File(UPLOAD_DIR + savedFilename);
	 * file.transferTo(saveFile);
	 * 
	 * // 이미지 URL 반환 String imageUrl = "/image/" + savedFilename; // 서버에서 제공할 이미지
	 * URL response.put("url", imageUrl); return new ResponseEntity<>(response,
	 * HttpStatus.OK); } catch (IOException e) { e.printStackTrace();
	 * response.put("error", "Image upload failed"); return new
	 * ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
	@ResponseBody
    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("upload") MultipartFile file) throws IOException {
        // 파일을 저장할 경로 설정
        String filePath = UPLOAD_DIR + file.getOriginalFilename();

        // 파일을 로컬 경로에 저장
        File dest = new File(filePath);
        file.transferTo(dest);

        // 저장된 파일의 경로를 반환
        return "{\"url\":\"http://localhost:8888/images/" + file.getOriginalFilename() + "\"}";
    }
    
}