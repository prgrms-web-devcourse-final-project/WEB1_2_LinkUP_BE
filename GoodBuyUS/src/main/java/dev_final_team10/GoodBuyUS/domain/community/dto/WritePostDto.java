package dev_final_team10.GoodBuyUS.domain.community.dto;


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
public class WritePostDto {
    private String title;
    private String category;
    private Long availableNumber;
    private Long period;
    private Long totalAmount;
    private Long unitAmount;
    private String description;
    private String productUrl;
    private List<String> imageUrls;
    private String imageUrl;

    public CommunityPost toEntity(User user, Neighborhood neighborhood){
        return CommunityPost.builder()
                .title(title)
                .category(category)
                .availableNumber(availableNumber)
                .period(LocalDateTime.now().plusDays(period))   //글 작성시간(현재시간) + 사용자가 입력한 모집일수 = 모집기간
                .totalAmount(totalAmount)
                .unitAmount(unitAmount)
                .description(description)
                .productUrl(productUrl)
                .imageUrls(imageUrls)
                .user(user)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .status(postStatus.NOT_APPROVED)
                .neighborhood(neighborhood).build();
    }
}
//글 작성 DTO