package dev_final_team10.GoodBuyUS.service;


import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AdminCommunityService {

    private final CommunityPostRepository communityPostRepository;

    //승인대기 중인 글 목록 확인
    public List<PostResponseDto> notApprovedList() {
        List<CommunityPost> communityPosts = communityPostRepository.findAll();

        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (CommunityPost communityPost : communityPosts) {
            if(communityPost.getStatus() == postStatus.NOT_APPROVED){
                postResponseDtos.add(PostResponseDto.of(communityPost));
            }
        }
        return postResponseDtos;
    }

    //승인 완료하기
    public PostResponseDto approvedPost(Long communityPostId) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId).orElse(null);
        communityPost.setStatus(postStatus.APPROVED);
        communityPost.setCreatedAt(LocalDateTime.now());
        communityPost.setCloseAt(LocalDateTime.now().plusDays(communityPost.getPeriod()));
        return PostResponseDto.of(communityPost);
    }

    public PostResponseDto rejectedPost(Long communityPostId) {
        CommunityPost communityPost = communityPostRepository.findById(communityPostId).orElse(null);
        communityPost.setStatus(postStatus.REJECTED);
        communityPost.setCreatedAt(LocalDateTime.now());
        communityPost.setCloseAt(LocalDateTime.now().plusDays(communityPost.getPeriod()));
        return PostResponseDto.of(communityPost);
    }
}
