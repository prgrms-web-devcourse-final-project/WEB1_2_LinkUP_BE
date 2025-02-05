package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.user.dto.UserSignUpDto;
import dev_final_team10.GoodBuyUS.service.CommunityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.repository.ParticipationsRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;


@Log4j2
@RestController
@AllArgsConstructor
@RequestMapping("/api/community")
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityPostRepository communityPostRepository;
    private final ParticipationsRepository participationsRepository;
    private final UserRepository userRepository;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();


    //공구 모집글 작성
    @PostMapping("/post")
    public ResponseEntity<?> writePost(@RequestPart("content") WriteModifyPostDto content,  // 나머지 데이터는 DTO(JSON)로 받기
                                       @RequestPart("images") List<MultipartFile> images) throws IOException {
        communityService.writePost(content, images);
        return ResponseEntity.ok(Map.of("message","글 작성이 완료되었습니다."));
    }

    //커뮤니티에서 모든 글 보기 (내 동네에 해당하는 글)
    @GetMapping("/post")
    public List<PostResponseDto> communityPostList(){
        return communityService.communityPostList();
    }

    //커뮤니티 특정 글 상세 보기
    @GetMapping("/post/{community_post_id}")
    public ResponseEntity<Map<String, Object>> readCommunityPost(@PathVariable Long community_post_id){
        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        User user = currentUser();
         Participations participations = participationsRepository.findByCommunityPostAndUser(communityPost, user);
        participationStatus participationStatus = null;
        boolean isWriter = false;

        if(user == communityPost.getUser()){
            isWriter = true;

        }
         if(participations != null){
             participationStatus = participations.getStatus();
         }

        //현재 남은 수량
        Long remainQuantity = communityPostRepository.findRemainingQuantity(community_post_id);

        Map<String, Object> response = new HashMap<>();
        response.put("communityPost", PostResponseDto.of(communityPost));
        response.put("participationStatus", participationStatus);
        response.put("isWriter", isWriter);
        response.put("remainQuantity", remainQuantity);

        return ResponseEntity.ok(response);

    }

    //SSE (실시간으로 보내주는 정보들) - 참여현황, 결제현황, 포스트상태, 참여자의 결제여부
    @GetMapping("/post/{community_post_id}/participants")
    public SseEmitter streamParticipants(@PathVariable Long community_post_id) throws IOException {
        // SseEmitter를 emitters에서 가져오거나, 없으면 새로 생성
        SseEmitter emitter = emitters.get(community_post_id);

        if (emitter == null) {
            // SseEmitter가 없으면 새로 생성하고 emitters에 추가
            emitter = new SseEmitter(Long.MAX_VALUE);
            emitters.put(community_post_id, emitter);
        }

// 항상 데이터를 전송하도록 변경
        sendStreamingData(community_post_id);

        // 이벤트가 완료되었을 때 처리
        emitter.onCompletion(() -> emitters.remove(community_post_id));
        emitter.onTimeout(() -> emitters.remove(community_post_id));




        return emitter;
    }

    public void sendStreamingData(Long community_post_id) throws IOException {
        Long participantCount = communityService.getParticipantCount(community_post_id);
        participantCount = (participantCount != null) ? participantCount : 0;

        Long paymentCount = communityService.getPaymentCount(community_post_id);
        paymentCount = (paymentCount != null) ? paymentCount : 0;

        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        postStatus postStatus = communityPost != null ? communityPost.getStatus() : null;

        Map<String, Object> data = new HashMap<>();
        data.put("participantCount", participantCount);
        data.put("paymentCount", paymentCount);
        data.put("postStatus", postStatus);

        emitters.get(community_post_id).send(SseEmitter.event().name("update").data(data));

    }


    //커뮤니티 게시글 참여하기
    @PostMapping("/post/{community_post_id}/join")
    public ResponseEntity<?> joinCommunityPost(@PathVariable Long community_post_id, @RequestBody  Map<String, Long> request) throws IOException {
        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        //이 글의 참여자 (참여했던 포함) 가져오기
        List<Participations> participations = participationsRepository.findAllByCommunityPost_CommunityPostId(community_post_id);

        //현재 사용자 정보 가져오기
        User user = currentUser();

        for (Participations participation : participations) {
            if (participation.getUser().equals(user)) {
                return ResponseEntity.badRequest().body(Map.of("message", "이미 참여하거나 취소한 글입니다."));
            }
        }
        Long participantCount = communityService.getParticipantCount(community_post_id);

        if(participantCount == communityPost.getAvailableNumber() ){
        return ResponseEntity.badRequest().body(Map.of("message","참여자수가 다 찼습니다"));

    }
    if(participantCount + request.get("number") > communityPost.getAvailableNumber()){
        return ResponseEntity.badRequest().body(Map.of("message","참여가능 수량을 초과했습니다"));

    }
    communityService.joinCommunityPost(communityPost, user, request.get("number"));
        sendStreamingData(community_post_id);
        return ResponseEntity.ok(Map.of("message","참여가 완료되었습니다."));

    }

    //커뮤니티 게시글 취소하기 (참여하기 후 취소)
    @PutMapping("/post/{community_post_id}/cancel")
    public ResponseEntity<?> cancelCommunityPost(@PathVariable Long community_post_id) throws IOException {
        CommunityPost communityPost = communityPostRepository.findById(community_post_id).orElse(null);
        //이 글의 참여자 (참여했던 포함) 가져오기
        List<Participations> participations = participationsRepository.findAllByCommunityPost_CommunityPostId(community_post_id);
        User user = currentUser();

        //현재 로그인한 사람의 참여 정보 가져오기(해당글의 참여정보)
        Participations participationInfo = null; // 초기값 설정
        for (Participations participation : participations) {
            if (participation.getUser().equals(user)) {
                participationInfo = participation;
                break;
            }
        }
        if (participationInfo.getStatus() == participationStatus.CANCEL) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 취소한 글입니다."));
        } else if (participationInfo.getStatus() == participationStatus.JOIN) {
            communityService.cancelCommunityPost(communityPost, user, participationInfo, participations);
            sendStreamingData(community_post_id);
            return ResponseEntity.ok(Map.of("message", "취소가 완료되었습니다."));
        }
        return ResponseEntity.badRequest().body(Map.of("message","취소를 할 수 없는 상태입니다."));
    }

    //현재 사용자 정보 가져오기
    private User currentUser(){
        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

}
