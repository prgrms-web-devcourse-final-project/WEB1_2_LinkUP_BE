package dev_final_team10.GoodBuyUS.controller;


import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.service.AdminCommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminCommunityService adminCommunityService;

    //승인대기 중인 글 조회
    @GetMapping("/post")
    public List<PostResponseDto> notApprovedList(){
        return adminCommunityService.notApprovedList();
    }
}
