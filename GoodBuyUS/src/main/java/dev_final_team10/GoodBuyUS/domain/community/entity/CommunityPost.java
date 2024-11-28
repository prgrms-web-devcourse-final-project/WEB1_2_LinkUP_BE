package dev_final_team10.GoodBuyUS.domain.community.entity;

import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
@Table(name = "community_post")
public class CommunityPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityPostId;   //기본키
    private String title;      //글제목 (상품명)
    private Long totalAmount;       //총가격
    private String description;     //상품 설명
    private String productUrl;  //상품 Url
    private String imageUrl;    //이미지 Url
    private String category;    //상품 카테고리       //추후 추가 예정
    private Long availableNumber;   //참여 가능 수량
    private Long participantNumber;     //현재 참여 인원 수
    private String createdAt;   //글 작성 시간
    private String modifiedAt;  //글 수정 시간
    private Long period;     //모집 기간
    private String place;       //수령 가능 지역
    private Long  personalAmount;   //한 개당 가격

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "neighborhood_code")
    private Neighborhood neighborhood;

}
