package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.repository.ParticipationsRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.service.CommunityService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityPostRepository communityPostRepository;
    private final ParticipationsRepository participationsRepository;
    private final UserRepository userRepository;

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

    //커뮤니티 특정 글 상세 보기
    @GetMapping("/post/{community_post_id}")
    public PostResponseDto readCommunityPost(@PathVariable Long community_post_id){
        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        return PostResponseDto.of(communityPost);
    }

    //커뮤니티 게시글 참여하기
    @PostMapping("/post/{community_post_id}/join")
    public ResponseEntity<?> joinCommunityPost(@PathVariable Long community_post_id, @RequestBody  Map<String, Long> request){
        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        //이 글의 참여자 (참여했던 포함) 가져오기
        List<Participations> participations = participationsRepository.findAllByCommunityPost_CommunityPostId(community_post_id);

        //이 글의 현재 참여자 가져오기 (참여수량 계산을 위해)
        List<Participations> joinParticipations = participationsRepository.findAllByCommunityPost_CommunityPostIdAndStatus(community_post_id, participationStatus.JOIN);

        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        Long count = 0L;

        for (Participations participation : participations) {
            if (participation.getUser() == user) {
                return ResponseEntity.badRequest().body(Map.of("message", "이미 참여하거나 취소한 글입니다."));
            }
        }
        for(Participations participation : joinParticipations){
            count += participation.getQuantity();

        }
        if(count == communityPost.getAvailableNumber() ){
        return ResponseEntity.badRequest().body(Map.of("message","참여자수가 다 찼습니다"));

    }
    if(count + request.get("number") > communityPost.getAvailableNumber()){
        return ResponseEntity.badRequest().body(Map.of("message","참여가능 수량을 초과했습니다"));

    }
    return communityService.joinCommunityPost(communityPost, user, request.get("number"));
    }

}
