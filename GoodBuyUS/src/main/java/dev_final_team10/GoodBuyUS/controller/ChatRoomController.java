package dev_final_team10.GoodBuyUS.controller;

import com.auth0.jwt.JWT;
import com.sun.security.auth.UserPrincipal;
import dev_final_team10.GoodBuyUS.domain.chat.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.chat.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.jwt.JwtService;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    //채팅방 생성
    @PostMapping()
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        return ResponseEntity.ok(chatRoomService.createChatRoom(chatRoomDTO));
    }

    //채팅방 삭제
    @DeleteMapping("/{chatRoomId}")
    public void removeChatRoom(@PathVariable Long chatRoomId) {
        chatRoomService.removeChatRoom(chatRoomId);
    }

    //채팅방 목록
    @GetMapping("/list")
    public List<ChatRoomDTO> chatRoomList(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow(()->new RuntimeException("액세스 토큰이 잘못되었습니다"));
        String email = jwtService.extractEmail(accessToken).orElseThrow(()->new RuntimeException("액세스 토큰이 잘못되었습니다"));
        Long userId = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다")).getId();

        return chatRoomService.getListChatRoom(userId);
    }
}
