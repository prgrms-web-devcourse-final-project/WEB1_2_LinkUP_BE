package dev_final_team10.GoodBuyUS.domain.community.dto;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCommentResponseDto {
    private Long id;
    private Long communityPostId;
    private String nickname;
    private String profile;
    private LocalDateTime createdAt;
    private Long parentId;
    private String content;

    public static CommunityCommentResponseDto of(CommunityComment communityComment){
        CommunityCommentResponseDto dto = new CommunityCommentResponseDto();
        dto.setId(communityComment.getCommentId());
        dto.setCommunityPostId(communityComment.getCommunityPost().getCommunityPostId());
        dto.setNickname(communityComment.getUser().getNickname());
        dto.setProfile(communityComment.getUser().getProfile());
        dto.setCreatedAt(communityComment.getCommentDate());
        dto.setParentId(communityComment.getParentCommentId());
        dto.setContent(communityComment.getCommentText());
        return dto;
    }
}
