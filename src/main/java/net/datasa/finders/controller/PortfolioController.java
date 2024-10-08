package net.datasa.finders.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.finders.domain.dto.FreelancerPortfoliosDTO;
import net.datasa.finders.security.AuthenticatedUser;
import net.datasa.finders.service.FreelancerPortfoliosService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("portfolio")
public class PortfolioController {
	
	private final FreelancerPortfoliosService freelancerPortfoliosService;

	@GetMapping("create")
    public String portfolio(Model model
    		,@AuthenticationPrincipal AuthenticatedUser user) {
		List<FreelancerPortfoliosDTO> freelancerPortfoliosDTOList = freelancerPortfoliosService.findPortfolioList(user.getId());
		
		model.addAttribute("portfoliosList", freelancerPortfoliosDTOList);
		
        return "/portfolio/create";
    }
	
	@PostMapping("save")
    public String save(@ModelAttribute FreelancerPortfoliosDTO FPDTO, @AuthenticationPrincipal AuthenticatedUser user) {
		freelancerPortfoliosService.save(FPDTO, user);
        return "/portfolio/portfolioList";
    }
	
	// 이미지 업로드 경로 설정 (로컬 경로 예시)
	private static final String UPLOAD_DIR = "C:/upload/portfolio/";

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
	
	/**
	 * 포트폴리오 작성시 임시로 저장되는 파일들 올라가는 장소 설정 및 에디터에 반환
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("upload") MultipartFile file) throws IOException {
		// 디렉토리 생성
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
		
        // 파일을 저장할 경로 설정
        String filePath = UPLOAD_DIR + file.getOriginalFilename();

        // 파일을 로컬 경로에 저장
        File dest = new File(filePath);
        file.transferTo(dest);

        // 저장된 파일의 경로를 반환
        return "{\"url\":\"http://localhost:8888/images/portfolio/" + file.getOriginalFilename() + "\"}";
    }
	
	@GetMapping("content")
    public String content(@RequestParam("portfolioId") int portfolioId
    		,@AuthenticationPrincipal AuthenticatedUser user
    		,Model model) {
		FreelancerPortfoliosDTO freelancerPortfoliosDTO = freelancerPortfoliosService.findPortfolioById(portfolioId);
		List<FreelancerPortfoliosDTO> freelancerPortfoliosDTOList = freelancerPortfoliosService.findPortfolioList(user.getId());
		
		model.addAttribute("portfoliosList", freelancerPortfoliosDTOList);
		
		model.addAttribute("freelancerPortfolios", freelancerPortfoliosDTO);
        return "portfolio/content";
    }
    
}
