package dev_final_team10.GoodBuyUS.domain.community.entity;

import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter

@Table(name = "community_comment")
public class CommunityComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CommentId; //댓글 고유 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_post_id")
    private CommunityPost communityPost;    //관련 커뮤니티 포스트 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  //댓글 작성자

    private LocalDateTime CommentDate;  //댓글 작성 시간
    private String CommentText; //댓글 내용
    private int Status; //댓글 상태

}
