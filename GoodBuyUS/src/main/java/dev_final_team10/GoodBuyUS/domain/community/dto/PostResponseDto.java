package dev_final_team10.GoodBuyUS.domain.community.dto;

import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityCategory;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class PostResponseDto {
    private Long communityPostId;
    private String title;
    private Long totalAmount;       //총가격
    private String description;     //상품 설명
    private String productUrl;  //상품 Url
    private CommunityCategory category;    //상품 카테고리       //추후 추가 예정
    private Long availableNumber;   //참여 가능 수량
    private LocalDateTime createdAt;   //글 작성 시간 - 승인이 완료된 시점(작성 시에는 null로 비워두기)
    private LocalDateTime period;     //모집 기간 = 글 승인 시간 + 글작성할 때 입력하는 일수
    private Long unitAmount;   //한 개당 가격
    private List<String> imageUrls = new ArrayList<>();    //이미지 Url
    private postStatus status; //글의 상태(승인대기, 승인완료 등등)

    //엔티티 -> DTO변환
    public static PostResponseDto of(CommunityPost communityPost) {
        PostResponseDto dto = new PostResponseDto();
        dto.setCommunityPostId(communityPost.getCommunityPostId());
        dto.setTitle(communityPost.getTitle());
        dto.setTotalAmount(communityPost.getTotalAmount());
        dto.setDescription(communityPost.getDescription());
        dto.setProductUrl(communityPost.getProductUrl());
        dto.setCategory(communityPost.getCategory());
        dto.setAvailableNumber(communityPost.getAvailableNumber());
        dto.setCreatedAt(communityPost.getCreatedAt());
        dto.setPeriod(communityPost.getPeriod());
        dto.setUnitAmount(communityPost.getUnitAmount());
        dto.setImageUrls(communityPost.getImageUrls());
        dto.setStatus(communityPost.getStatus());

        return dto;
    }
}
