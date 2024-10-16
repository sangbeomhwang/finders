package net.datasa.finders.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.datasa.finders.domain.dto.ChatRoomDTO;
import net.datasa.finders.domain.dto.CreateChatRoomRequestDTO;
import net.datasa.finders.domain.dto.InviteRequestDTO;
import net.datasa.finders.domain.dto.ProjectDTO;
import net.datasa.finders.domain.entity.ChatParticipantEntity;
import net.datasa.finders.domain.entity.ChatRoomEntity;
import net.datasa.finders.domain.entity.ProjectEntity;
import net.datasa.finders.repository.ChatParticipantRepository;
import net.datasa.finders.repository.ChatRoomRepository;
import net.datasa.finders.repository.ProjectRepository;
import net.datasa.finders.security.AuthenticatedUser;
import net.datasa.finders.service.ChatRoomService;

//채팅전용
@Controller
@RequestMapping("chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository; // 이 레포지토리도 정의되어야 함
    private final ProjectRepository projectRepository; // 이 레포지토리도 정의되어야 함
  
    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService, 
                              ChatParticipantRepository chatParticipantRepository, 
                              ChatRoomRepository chatRoomRepository, 
                              ProjectRepository projectRepository) {
        this.chatRoomService = chatRoomService;
        this.chatParticipantRepository = chatParticipantRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.projectRepository = projectRepository;
    }

    // 현재 사용자가 속한 채팅방 목록 페이지
    @GetMapping("list")
    public String showChatRooms(Model model) {
        // 현재 로그인한 사용자의 ID 가져오기
        String currentUserId = getCurrentUserId();
        
        // 현재 사용자가 속한 채팅방만 가져오기
        List<ChatRoomEntity> chatRooms = chatRoomService.getChatRoomsForMember(currentUserId);
        model.addAttribute("chatrooms", chatRooms);
        return "chat/chatrooms"; // chatrooms.html로 이동
    }
    
	@GetMapping("view")
    public String chatview() {
		
        return "/chat/view";
    }

//    @GetMapping("/create")
//    public String createChatRoomPage(Model model) {
//        // 현재 로그인한 사용자 ID 가져오기
//        String currentUserId = getCurrentUserId();
//        
//        // 사용자가 참여하고 있는 프로젝트 목록 가져오기
//        List<ProjectDTO> projects = chatRoomService.getProjectsForMember(currentUserId);
//        
//        // 모델에 프로젝트 목록 추가
//        model.addAttribute("projects", projects);
//        return "create-chatroom"; // create-chatroom.html로 이동
//    }

//    // member_id에 기반해 채팅방 생성
//    @PostMapping("/create")
//    public ResponseEntity<String> createChatRoomsForMember(@RequestParam("memberId") String memberId) {
//        chatRoomService.createChatRoomsForAllMemberProjects(memberId);
//        return ResponseEntity.ok("채팅방이 성공적으로 생성되었습니다.");
//    }

    @GetMapping("/room")
    public String getChatRoom(@RequestParam("id") int chatroomId, Model model) {
        ChatRoomEntity chatRoom = chatRoomService.getChatRoomById(chatroomId);
        if (chatRoom != null) {
            String currentUserId = getCurrentUserId();
            int projectNum = chatRoom.getProjectNum(); // projectNum 가져오기
            model.addAttribute("chatroomName", chatRoom.getChatroomName());
            model.addAttribute("chatroomId", chatroomId);
            model.addAttribute("projectNum", projectNum); // projectNum 추가
            model.addAttribute("memberId", currentUserId);
            return "chat/chatroom"; // chatroom.html로 이동
        } else {
            return "error"; // 채팅방이 존재하지 않는 경우 에러 페이지로 이동
        }
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
            return user.getId(); // 사용자 ID 반환
        }
        return "unknown"; // 로그인하지 않은 경우
    }
    
    @GetMapping("/getProjects")
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        // 현재 로그인한 사용자 ID 가져오기
        String currentUserId = getCurrentUserId();
        
        // 사용자가 참여하고 있는 프로젝트 목록 가져오기
        List<ProjectDTO> projects = chatRoomService.getProjectsForMember(currentUserId);
        
        return ResponseEntity.ok(projects);
    }
    
    @PostMapping("/createChatRoom")
    public ResponseEntity<Void> createChatRoom(
            @RequestBody CreateChatRoomRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails) { // 현재 로그인한 사용자 정보 가져오기

        String loggedInUserId = userDetails.getUsername(); // 로그인한 사용자 ID 가져오기
        chatRoomService.createChatRoom(request, loggedInUserId); // 로그인한 사용자 ID를 서비스로 전달

        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/getTeamMembers")
    public ResponseEntity<List<String>> getTeamMembers(@RequestParam(value = "projectNum", required = false) Integer projectNum) {
        if (projectNum == null) {
            return ResponseEntity.badRequest().body(List.of("유효하지 않은 프로젝트 번호입니다.")); // 적절한 에러 메시지
        }

        String currentUserId = getCurrentUserId(); // 현재 로그인한 사용자 ID 가져오기
        List<String> teamMembers = chatRoomService.getTeamMembersByProjectNum(projectNum, currentUserId);
        return ResponseEntity.ok(teamMembers);
    }
    
    @GetMapping("/getChatRooms")
    @ResponseBody
    public List<ChatRoomDTO> getChatRooms() {
        String currentUserId = getCurrentUserId(); // 로그인한 사용자 ID 가져오기
        // 현재 로그인한 사용자가 속한 채팅방만 반환
        return chatRoomService.getChatRoomsForLoggedInUser(currentUserId);
    }
    
    @GetMapping("/participants")
    @ResponseBody
    public ResponseEntity<List<String>> getChatParticipants(@RequestParam("chatroomId") int chatroomId) {
        List<String> participants = chatRoomService.getParticipantsByChatroomId(chatroomId);
        return ResponseEntity.ok(participants);
    }
    
    @GetMapping("/getAvailableTeamMembers")
    @ResponseBody
    public ResponseEntity<List<String>> getAvailableTeamMembers(@RequestParam("projectNum") int projectNum, 
                                                                @RequestParam("chatroomId") int chatroomId) {
        List<String> availableMembers = chatRoomService.getAvailableTeamMembers(projectNum, chatroomId);
        return ResponseEntity.ok(availableMembers);
    }
    
    @PostMapping("/invite")
    @ResponseBody
    public ResponseEntity<String> inviteMemberToChatRoom(@RequestBody InviteRequestDTO request) {
        chatRoomService.inviteMember(request.getChatroomId(), request.getMemberId());
        return ResponseEntity.ok("초대가 완료되었습니다.");
    }
    
    @GetMapping("/getProjectNum")
    public ResponseEntity<Map<String, Object>> getProjectNum(@RequestParam("chatroomId") int chatroomId) {
        // chatroomId에 맞는 projectNum 조회
        Integer projectNum = chatRoomService.findProjectNumByChatroomId(chatroomId);
        if (projectNum != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("projectNum", projectNum);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // 현재 로그인한 사용자가 속한 채팅방만 가져오는 메서드
    public List<ChatRoomDTO> getChatRoomsForLoggedInUser(String userId) {
        List<Integer> userChatroomIds = chatParticipantRepository.findByParticipantId(userId)
                .stream()
                .map(ChatParticipantEntity::getChatroomId)
                .distinct()
                .collect(Collectors.toList());

        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllById(userChatroomIds);

        // 프로젝트 데이터를 조회하고 map에 저장
        List<ProjectEntity> projects = projectRepository.findAll();
        Map<Integer, String> projectMap = projects.stream()
            .collect(Collectors.toMap(ProjectEntity::getProjectNum, ProjectEntity::getProjectName));

        return chatRooms.stream()
            .map(room -> {
                // projectNum으로 프로젝트 제목을 가져옴
                String projectTitle = projectMap.get(room.getProjectNum());
                System.out.println("ChatRoomId: " + room.getChatroomId() + ", ProjectNum: " + room.getProjectNum() + ", ProjectTitle: " + projectTitle);

                return ChatRoomDTO.builder()
                    .chatroomId(room.getChatroomId())
                    .chatroomName(room.getChatroomName())
                    .projectNum(room.getProjectNum())
                    .projectTitle(projectTitle != null ? projectTitle : "제목 없음") // null일 경우 "제목 없음" 설정
                    .createdTime(Timestamp.valueOf(room.getCreatedTime()))
                    .build();
            })
            .collect(Collectors.toList());
    }
    
 // ChatRoomController.java
    @PostMapping("/leave")
    public ResponseEntity<String> leaveChatRoom(@RequestParam("chatroomId") int chatroomId) {
        // 현재 로그인한 사용자 ID 가져오기
        String currentUserId = getCurrentUserId();
        System.out.println("Current User ID: " + currentUserId);  // 로그로 출력

        // chatroomId와 currentUserId에 해당하는 참여 기록을 삭제
        int deletedCount = chatParticipantRepository.deleteByChatroomIdAndParticipantId(chatroomId, currentUserId);

        if (deletedCount > 0) {
            // 참가자가 나간 후 참가자 수 확인 및 삭제 메서드 호출
            chatRoomService.deleteChatRoomIfNoParticipants(chatroomId);
            return ResponseEntity.ok("채팅방을 나갔습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("채팅방 나가기 실패");
        }
    }
    
    @DeleteMapping("/deleteIfNoParticipants")
    public ResponseEntity<Void> deleteIfNoParticipants(@RequestParam int chatRoomId) {
        chatRoomService.deleteChatRoomIfNoParticipants(chatRoomId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/updateLastReadTime")
    public ResponseEntity<Void> updateLastReadTime(
            @RequestParam("chatroomId") int chatroomId,
            @RequestParam("memberId") String memberId) {
        try {
            // last_read_time 업데이트 로직
            chatRoomService.updateLastReadTime(chatroomId, memberId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

  

    @GetMapping("/check-new-messages")
    public ResponseEntity<Map<String, Integer>> checkNewMessages(@RequestParam("chatroomId") int chatroomId, @AuthenticationPrincipal AuthenticatedUser user) {
        String userId = user.getId();
        
        // 새로운 메시지의 갯수를 조회
        int newMessageCount = chatRoomService.countNewMessages(chatroomId, userId);
        
        // 결과를 Map에 담아 반환
        Map<String, Integer> response = new HashMap<>();
        response.put("newMessageCount", newMessageCount);
        
        return ResponseEntity.ok(response);
    }
    
}
