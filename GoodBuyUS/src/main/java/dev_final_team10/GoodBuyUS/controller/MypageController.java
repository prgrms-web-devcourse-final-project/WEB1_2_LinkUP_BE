package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final CommunityPostRepository communityPostRepository;

    //현재 비밀번호 일치하는지 확인 - 비밀번호 변경 전에
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String, String> request){
        //현재 로그인된 사용자 email 가져오기
        String userEmail = mypageService.getCurrentUserEmail();

        //현재 비밀번호가 맞는지 확인
        if(!mypageService.isCurrentPasswordValid(userEmail, request.get("currentPassword"))){
            return ResponseEntity.badRequest().body(Map.of("error","현재 비밀번호가 일치하지 않습니다."));
        }

        //현재 비밀번호가 맞으면 true 반환
        return ResponseEntity.ok(Map.of("valid", true));
    }


    //비밀번호 변경
    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> request){
        String userEmail = mypageService.getCurrentUserEmail();

        mypageService.updateNewPassword(userEmail, request.get("newPassword"));
        return ResponseEntity.ok(Map.of("message","비밀번호가 성공적으로 변경되었습니다."));
    }

    //동네 변경
    @PutMapping("/changeneighbor")
    public ResponseEntity<?> changeNeighbor(@RequestBody Map<String, String> request){
        return mypageService.chageNeighbor(request.get("newAddress"));
    }

    //작성한 글이 승인대기 상태일 때 수정가능 하도록
    @PutMapping("/post/{community_post_id}")
    public ResponseEntity<?> modifyPost(@PathVariable("community_post_id") Long communityPostId, @RequestBody WriteModifyPostDto writeModifyPostDto){
        CommunityPost communityPost = communityPostRepository.findById(communityPostId).orElse(null);
        //현재 글의 상태가 승인대기가 아니라면 글을 수정하지 못하도록
        if(communityPost.getStatus() != postStatus.NOT_APPROVED){
            return ResponseEntity.badRequest().body(Map.of("error","글을 수정할 수 없는 상태입니다."));
        }
        PostResponseDto postResponseDto = mypageService.modifyPost(writeModifyPostDto, communityPostId);
        return ResponseEntity.ok(Map.of("message", "글이 수정되었습니다.",
                "updatedPost", postResponseDto));
    }
}
