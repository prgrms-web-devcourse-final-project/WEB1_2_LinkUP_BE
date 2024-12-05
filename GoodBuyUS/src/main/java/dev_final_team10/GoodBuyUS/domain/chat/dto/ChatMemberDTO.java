package dev_final_team10.GoodBuyUS.domain.chat.dto;

import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatMember;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.payment.entity.CommunityPayment;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMemberDTO {
    private String orderId;
    private Long userId;
    private String status;

    public static ChatMember toEntity(CommunityPost post, ChatRoom room, User user){
        return ChatMember.builder()
                .chatRoom(room)
                .post(post)
                .user(user)
                .build();
    }
}
