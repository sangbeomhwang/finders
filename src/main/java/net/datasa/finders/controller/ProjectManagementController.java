package net.datasa.finders.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datasa.finders.domain.dto.FunctionTitleDTO;
import net.datasa.finders.domain.dto.ProjectPublishingDTO;
import net.datasa.finders.domain.dto.TaskManagementDTO;
import net.datasa.finders.domain.entity.RoleName;
import net.datasa.finders.security.AuthenticatedUser;
import net.datasa.finders.service.ProjectManagementService;

/**
 * 거래 게시판 관련 콘트롤러
 */

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("myProject")
public class ProjectManagementController {
	
	private final ProjectManagementService projectManagementService;
	
	@GetMapping("view")
	public String view() {
		return "project/listView";
	}
	
	@ResponseBody
    @GetMapping("projectList")
    public List<ProjectPublishingDTO> projectList(@AuthenticationPrincipal AuthenticatedUser user) {
        //서비스로 사용자 아이디를 전달하여 해당 아이디의 수입,지출 내역을 목록으로 리턴한다.
        List<ProjectPublishingDTO> projectList = projectManagementService.getMyList(user.getUsername());
        
        return projectList;
    }
    
    @GetMapping("management")
	public String read(@RequestParam("projectNum") int pNum
			, Model model
			, @AuthenticationPrincipal AuthenticatedUser user) {
	    try {
            RoleName roleName = RoleName.valueOf(user.getRoleName());

            log.debug("현재 사용자의 역할: {}", roleName);
	        ProjectPublishingDTO projectPublishingDTO = projectManagementService.getBoard(pNum, user.getUsername(), roleName);
	        model.addAttribute("board", projectPublishingDTO);
            model.addAttribute("user", user);
            model.addAttribute("roleName", projectPublishingDTO.getRoleName());
	        return "project/management";
	    } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/myProject/view";
	    }
	}
    
    /*
    @ResponseBody
    @PostMapping("saveFunction")
    public FunctionTitleDTO saveFunction(@RequestParam("projectNum") int projectNum,
                                          @RequestBody FunctionTitleDTO functionTitleDTO) {
        try {
            // projectNum과 titleName을 DTO에서 추출
            String functionTitleName = functionTitleDTO.getTitleName();
            
            // 서비스 메서드를 호출하여 기능을 저장
            FunctionTitleDTO dto = projectManagementService.saveFunction(projectNum, functionTitleName);
            return dto;
        } catch (Exception e) {
            log.error("Error saving function", e);
            return null;
        }
    }
    
    @ResponseBody
    @PostMapping("saveTask")
    public TaskManagementDTO saveTask(
            @RequestParam("projectNum") int projectNum,
            @RequestBody TaskManagementDTO taskDTO) {
        
        try {
            log.debug("projectNum controller 체크용: {}", projectNum);
            log.debug("taskDTO controller 체크용: {}", taskDTO);

            TaskManagementDTO savedTask = projectManagementService.saveTask(projectNum, taskDTO);

            log.debug("taskManagementDTO controller 체크용2: {}", savedTask);
            
            return savedTask;

        } catch (IllegalArgumentException e) {
            log.error("Duplicate task title or role error: ", e);
            return null;
        } catch (Exception e) {
            log.error("Error saving task: ", e);
            return null;
        }
    }
    */
    
    @ResponseBody
    @PostMapping("saveFunctionAndTask")
    public ResponseEntity<?> saveFunctionAndTask(
            @RequestParam("projectNum") int projectNum,
            @RequestBody TaskManagementDTO taskDTO) {

        try {
        	// FunctionTitleName이 request body에 포함된 경우 
            String functionTitleName = taskDTO.getFunctionTitleName();

            // 서비스 계층에서 기능 제목과 업무 데이터 저장 처리
            FunctionTitleDTO savedFunction = projectManagementService.saveFunctionAndTask(projectNum, functionTitleName, taskDTO);
            
            return ResponseEntity.ok(savedFunction);
        } catch (IllegalArgumentException e) {
            log.error("Validation error: ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error saving function and task: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving function and task");
        }
    }
    
    @ResponseBody
    @GetMapping("loadFunctionTitles")
    public ResponseEntity<List<FunctionTitleDTO>> loadFunctionTitles(@RequestParam(name = "projectNum") int projectNum) {
        try {
            
        	List<FunctionTitleDTO> functionTitles = projectManagementService.getAllFunctionTitles(projectNum);
            
        	return ResponseEntity.ok(functionTitles);
        
        } catch (Exception e) {
            
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        
        }
    }
    
    @ResponseBody
    @GetMapping("getTasks")
    public List<TaskManagementDTO> getTasks(@RequestParam("projectNum") int projectNum) {
        try {
            return projectManagementService.getTasks(projectNum);
        } catch (Exception e) {
            log.error("Error retrieving tasks", e);
            return Collections.emptyList();
        }
    }
    
    @ResponseBody
    @PostMapping("completeProject")
    public String completeProject(@RequestParam("projectNum") int projectNum) {
    	
    	try {
            projectManagementService.projectCompletion(projectNum);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }

    }
    
    @ResponseBody
    @GetMapping("getGanttChartData")
    public Map<String, Object> getGanttChartData(@RequestParam("projectNum") int projectNum) {
        return projectManagementService.getGanttChartData(projectNum);
    }
    
    @ResponseBody
    @GetMapping("loadEntityNames")
    public List<?> loadEntityNames(@RequestParam("projectNum") int projectNum
    		, @RequestParam("entityType") String entityType) {
    	
    	// entityType에 따라 다른 데이터 반환 로직
        if ("task".equalsIgnoreCase(entityType)) {
            // TaskManagementEntity 리스트 반환
            return projectManagementService.getTasks(projectNum);
        } else if ("function".equalsIgnoreCase(entityType)) {
            // FunctionTitleEntity 리스트 반환
            return projectManagementService.getFunctions(projectNum);
        }
        // 유효하지 않은 entityType일 경우 빈 리스트 반환
        return Collections.emptyList();
    }
    
    @ResponseBody
    @PostMapping("updateProgress")
    public String updateProgress(@RequestParam("entityType") String entityType,
    							 @RequestParam("id") int dbId,
                                 @RequestParam("progressValue") String progressValue) {
        try {
        	projectManagementService.updateProgress(entityType, dbId, progressValue);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "failure";
        }
    }
    

    // 임시 리스트 화면 구현 시 기존 프로젝트 생성 페이지 Controller 코드
    /*
    @GetMapping("write")
    public String write() {
        return "project/writeForm";
    }
     
    @PostMapping("write")
    public String write(
            @ModelAttribute ProjectPublishingDTO projectPublishingDTO,
            @RequestParam("projectImageFile") MultipartFile projectImageFile, // 이미지 파일
            @RequestParam("selectedSkills") String selectedSkills,  // 관련 기술
            @RequestParam("projectDescription") String projectDescription,  // 상세 업무 내용
            @RequestParam("projectBudget") BigDecimal projectBudget,  // 지출 예산
            @RequestParam("projectStartDate") LocalDate projectStartDate,  // 프로젝트 시작일
            @RequestParam("projectEndDate") LocalDate projectEndDate,  // 프로젝트 종료일
            @RequestParam("recruitDeadline") LocalDateTime recruitDeadline,
            @RequestParam("role") List<String> roles, // 모집 인원 역할
            @RequestParam("category") List<String> categories, // 모집 인원 카테고리
            @RequestParam("teamSize[]") List<Integer> teamSizes, // 모집 인원
            @RequestParam("question[]") List<String> questions, // 사전 질문
            @AuthenticationPrincipal AuthenticatedUser user) {

        // 작성한 글에 사용자 아이디 추가
        projectPublishingDTO.setClientId(user.getUsername());

        // BoardService 호출해서 프로젝트 및 관련 데이터 저장
        boardService.write(projectPublishingDTO, projectImageFile, selectedSkills
                , projectDescription, projectBudget, projectStartDate, projectEndDate, recruitDeadline, roles, categories, teamSizes, questions);

        return "redirect:/myProject/view";
    }

	@GetMapping("read")
	public String read(@RequestParam("projectNum") int pNum, Model model, @AuthenticationPrincipal AuthenticatedUser user) {
	    try {
            RoleName roleName = RoleName.valueOf(user.getRoleName());

            log.debug("현재 사용자의 역할: {}", roleName);
	        ProjectPublishingDTO projectPublishingDTO = boardService.getBoard(pNum, user.getUsername(), roleName);
	        model.addAttribute("board", projectPublishingDTO);
            model.addAttribute("user", user);
            model.addAttribute("roleName", projectPublishingDTO.getRoleName());
	        return "project/read";
	    } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/myProject/view";
	    }
	}

	@PostMapping("delete")
    public String deletePost(@RequestParam("projectNum") int pNum, @AuthenticationPrincipal AuthenticatedUser user) {
        try {
            RoleName roleName = RoleName.valueOf(user.getRoleName());
            ProjectPublishingDTO projectPublishingDTO = boardService.getBoard(pNum, user.getUsername(), roleName);

            // 로그인된 사용자와 게시글 작성자가 같은지 확인
            if (projectPublishingDTO.getClientId().equals(user.getUsername())) {
                boardService.deleteBoard(pNum);
                return "redirect:/myProject/view"; // 게시글 목록 페이지로 리다이렉트
            } else {
                return "redirect:/myProject/view?error=deletePermission"; // 권한 오류 페이지로 리다이렉트
            }
        } catch (Exception e) {
            return "redirect:/myProject/view?error=deleteFailed"; // 삭제 실패 페이지로 리다이렉트
        }
    }
    */
}
