package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.chat.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message/{chatRoomId}") //이 주소로 발행된 메세지를
//    @SendTo("/sub/message/{chatRoomId}")  // 이 주소를 구독한 사용자에게 보냄
    public void sendMessage(@DestinationVariable("chatRoomId") String chatRoomId, MessageDTO messageDTO) {
        simpMessagingTemplate.convertAndSend("/sub/message/" + chatRoomId, messageDTO);
    }
}