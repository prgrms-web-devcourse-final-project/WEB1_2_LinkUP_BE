package dev_final_team10.GoodBuyUS.domain.chat.dto;

import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private Long postId;
    private int capacity;
    private String roomName;
    private List<String> chatMembers;

    public ChatRoom toEntity(CommunityPost post){
        return ChatRoom.builder()
                .roomName(post.getTitle()+"의 채팅방")
                .post(post)
                .capacity(this.capacity)
                .build();
    }
}
