package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.MessageDTO;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/message/{chatRoomId}") //이 주소로 발행된 메세지를
    @SendTo("/sub/message/{chatRoomId}")  // 이 주소를 구독한 사용자에게 보냄
    public String sendMessage(@DestinationVariable String chatRoomId, MessageDTO messageDTO) {
        return messageDTO.getUserName() + ":" + messageDTO.getMessage();
    }
}