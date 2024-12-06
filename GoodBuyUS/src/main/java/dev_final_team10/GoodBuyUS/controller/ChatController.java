package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatDTO;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/message/{chatRoomId}") //이 주소로 발행된 메세지를
//    @SendTo("/sub/message/{chatRoomId}")  // 이 주소를 구독한 사용자에게 보냄
    public void sendMessage(@DestinationVariable("chatRoomId") Long chatRoomId, ChatDTO chatDTO) {

        ChatDTO chatDTO2 = chatService.createChat(chatDTO, chatRoomId);
        simpMessagingTemplate.convertAndSend("/sub/message/" + chatRoomId, chatDTO2);
    }

}