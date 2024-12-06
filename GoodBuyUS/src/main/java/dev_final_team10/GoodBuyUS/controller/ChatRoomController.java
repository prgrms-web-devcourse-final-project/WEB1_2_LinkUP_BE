package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatDTO;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.ChatRoomService;
import dev_final_team10.GoodBuyUS.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ChatService chatService;

    //채팅방 생성
    @PostMapping("/chat")
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(chatRoomDTO));
    }

    //채팅 메시지 조회
    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<List<ChatDTO>> getAllChat(@PathVariable Long chatRoomId) {
        List<ChatDTO> chatDTOList = chatService.getAllChat(chatRoomId);
        return ResponseEntity.ok(chatDTOList);
    }

        //채팅방 삭제
    @DeleteMapping("/chat/{chatRoomId}")
    public void removeChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.removeChatRoom(chatRoomId);
    }

    //내 채팅방 목록
    @GetMapping("/mypage/chatlist")
    public List<ChatRoomDTO> chatRoomList(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(()->new RuntimeException("액세스 토큰이 잘못되었습니다"));
        String email = jwtService.extractEmail(accessToken).orElseThrow(()->new RuntimeException("액세스 토큰이 잘못되었습니다"));
        Long userId = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다")).getId();

        return chatRoomService.getListChatRoom(userId);
    }

    //채팅방 전체 조회 - 관리자
    @GetMapping("/admin/chatlist")
    public List<ChatRoomDTO> chatRoomListAll() {
        return chatRoomService.getListAllChatRoom();
    }
}
