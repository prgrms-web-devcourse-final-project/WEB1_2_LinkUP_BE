package dev_final_team10.GoodBuyUS.domain.chat;

import dev_final_team10.GoodBuyUS.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private Long postId;
    private int capacity;
    private String roomName;

    public ChatRoom toEntity(Post post){
        return ChatRoom.builder()
                .roomName(roomName)
                .post(post)
                .capacity(capacity)
                .build();
    }


}
