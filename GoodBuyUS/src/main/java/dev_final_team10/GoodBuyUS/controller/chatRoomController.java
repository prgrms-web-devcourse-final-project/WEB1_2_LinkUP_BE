package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class chatRoomController {
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
}
