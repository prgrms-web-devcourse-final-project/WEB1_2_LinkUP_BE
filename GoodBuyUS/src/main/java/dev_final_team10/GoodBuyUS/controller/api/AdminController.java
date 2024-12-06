package dev_final_team10.GoodBuyUS.controller.api;


import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.service.AdminCommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminCommunityService adminCommunityService;

    //승인대기 중인 글 조회
    @GetMapping("/post")
    public List<PostResponseDto> notApprovedList(){
        return adminCommunityService.notApprovedList();
    }

    //승인대기 중인 글 승인 완료 하기
    @PatchMapping("/post/approve/{community_post_id}")
    public PostResponseDto approvedPost(@PathVariable Long community_post_id) throws IOException {
        return adminCommunityService.approvedPost(community_post_id);
    }
    
    //승인 거절
    @PatchMapping("post/reject/{community_post_id}")
    public PostResponseDto rejectedPost(@PathVariable Long community_post_id) throws IOException {
        return adminCommunityService.rejectedPost(community_post_id);
    }
}
