package dev_final_team10.GoodBuyUS.domain.chat.entity;

import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "chat")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    private String sender;

    private String message;

    private OffsetDateTime time;

    public static ChatDTO toDTO(Chat chat) {
        return ChatDTO.builder()
                .roomId(chat.getChatRoom().getId())
                .message(chat.getMessage())
                .userName(chat.getSender())
                .time(chat.getTime())
                .build();
    }

}
