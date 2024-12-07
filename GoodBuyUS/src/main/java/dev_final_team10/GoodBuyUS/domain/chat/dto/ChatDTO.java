package dev_final_team10.GoodBuyUS.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev_final_team10.GoodBuyUS.domain.chat.entity.Chat;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDTO {
    private Long roomId;
    private String userName;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime time;

    public static Chat toEntity(ChatDTO chatDTO, ChatRoom chatRoom){
        return Chat.builder()
                .chatRoom(chatRoom)
                .sender(chatDTO.getUserName())
                .message(chatDTO.getMessage())
                .time(chatDTO.getTime()).build();
    }
}
