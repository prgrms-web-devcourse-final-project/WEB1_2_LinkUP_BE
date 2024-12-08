package dev_final_team10.GoodBuyUS.domain.community.entity;

import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityPostId;   //기본키
    private String title;      //글제목 (상품명)
    private Long totalAmount;       //총가격
    private String description;     //상품 설명
    private String productUrl;  //상품 Url
    @Enumerated(EnumType.STRING)
    private CommunityCategory category;    //상품 카테고리       //추후 추가 예정
    private Long availableNumber;   //참여 가능 수량
    private LocalDateTime createdAt;   //글 작성 시간 - 승인이 완료된 시점(작성 시에는 작성 시점으로)
    private Long period;     //모집일
    private LocalDateTime closeAt;  //모집 기간 = 글 승인 시간 + 글작성할 때 입력하는 일수
    private Long unitAmount;   //한 개당 가격
    private LocalDateTime paymentDeadline;  //결제 마감 기한 = 모집 다 된 시간 + 12시간


    @ElementCollection
    @CollectionTable(name = "community_post_image", joinColumns = @JoinColumn(name = "community_post_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();    //이미지 Url


    @Enumerated(EnumType.STRING)
    private postStatus status; //글의 상태(승인대기, 승인완료 등등)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  //글작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_code")
    private Neighborhood neighborhood;  //글작성자의 동네코드


    public void updateFields(WriteModifyPostDto writeModifyPostDto, User user, Neighborhood neighborhood, CommunityCategory communityCategory ) {
        this.title = writeModifyPostDto.getTitle();
        this.availableNumber = writeModifyPostDto.getAvailableNumber();
        this.category = communityCategory;
        this.totalAmount = writeModifyPostDto.getTotalAmount();
        this.productUrl = writeModifyPostDto.getProductUrl();
        this.description = writeModifyPostDto.getDescription();
        this.unitAmount = writeModifyPostDto.getUnitAmount();
        this.user = user;
        this.neighborhood = neighborhood;
        this.imageUrls = writeModifyPostDto.getImageUrls();
        this.createdAt = LocalDateTime.now();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPaymentDeadline(LocalDateTime paymentDeadline) {
        this.paymentDeadline = paymentDeadline;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(postStatus status) {
        this.status = status;
    }

    public void setCloseAt(LocalDateTime closeAt) {
        this.closeAt = closeAt;
    }
}
