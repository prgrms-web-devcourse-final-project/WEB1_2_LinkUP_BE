package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.chat.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.chat.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

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
    @GetMapping("/{userId}")
    public List<ChatRoomDTO> chatRoomList(@PathVariable Long userId) {
        return chatRoomService.getListChatRoom(userId);
    }
}
