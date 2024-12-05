package dev_final_team10.GoodBuyUS.domain.community.entity;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "participations")
public class Participations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participationId;   //기본키
    private Long quantity;  //참여 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;

    @Enumerated(EnumType.STRING)
    private participationStatus status;

    private boolean isWriter;
}
