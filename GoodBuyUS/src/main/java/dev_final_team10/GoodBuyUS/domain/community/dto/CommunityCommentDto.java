package dev_final_team10.GoodBuyUS.domain.community.dto;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityComment;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCommentDto {
    private String content;
    private Long parentId;

    //DTO -> Entity
    public CommunityComment toEntityForCreate(CommunityPost post, User user,Long parentId, String comment){
        return CommunityComment.builder()
                .communityPost(post)
                .user(user)
                .commentDate(LocalDateTime.now())
                .parentCommentId(parentId)
                .commentText(comment).build();
    }
}
