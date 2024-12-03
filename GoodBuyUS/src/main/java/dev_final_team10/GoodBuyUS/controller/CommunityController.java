package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    //공구 모집글 작성
    @PostMapping("/post")
    public ResponseEntity<?> writePost(@RequestBody WriteModifyPostDto writeModifyPostDto){
        communityService.writePost(writeModifyPostDto);
        return ResponseEntity.ok(Map.of("message","글 작성이 완료되었습니다."));
    }

    //커뮤니티에서 모든 글 보기 (내 동네에 해당하는 글)
    @GetMapping("/post")
    public List<PostResponseDto> communityPostList(){
        return communityService.communityPostList();
    }
}
