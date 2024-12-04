package dev_final_team10.GoodBuyUS.domain.community.dto;


import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityCategory;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class WriteModifyPostDto {
    private String title;
    private String category;
    private Long availableNumber;
    private Long period;
    private Long totalAmount;
    private Long unitAmount;
    private String description;
    private String productUrl;
    private List<String> imageUrls;

    //글 작성 시 사용할 메소드
    public CommunityPost toEntityForCreate(User user, Neighborhood neighborhood, CommunityCategory communityCategory){
        return CommunityPost.builder()
                .title(title)
                .category(communityCategory)
                .availableNumber(availableNumber)
                .totalAmount(totalAmount)
                .unitAmount(unitAmount)
                .description(description)
                .productUrl(productUrl)
                .imageUrls(imageUrls)
                .user(user)
                .period(period)
                .createdAt(LocalDateTime.now())
                //글 상태 - 승인대기
                .status(postStatus.NOT_APPROVED)
                .neighborhood(neighborhood).build();
    }

}
//글 작성, 수정 DTO