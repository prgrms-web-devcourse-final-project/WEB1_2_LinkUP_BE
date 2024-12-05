package dev_final_team10.GoodBuyUS.service;


import dev_final_team10.GoodBuyUS.domain.community.dto.PostResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.WriteModifyPostDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.*;
import dev_final_team10.GoodBuyUS.domain.user.entity.Neighborhood;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.repository.ParticipationsRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Log4j2
@Transactional
@RequiredArgsConstructor
@Service
public class CommunityService {
    private final UserRepository userRepository;
    private final CommunityPostRepository communityPostRepository;
    private final ParticipationsRepository participationsRepository;


    //글 작성 메소드
    public void writePost(WriteModifyPostDto writeModifyPostDto) {
        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        //현재 사용자 동네 정보 가져오기
        Neighborhood neighborhood = user.getNeighborhood();
        //카테고리 설정
        CommunityCategory communityCategory = CommunityCategory.fromString(writeModifyPostDto.getCategory());
        //DTO -> entity로 변환
        CommunityPost communityPost = writeModifyPostDto.toEntityForCreate(user, neighborhood, communityCategory);


        //DB 저장
        communityPostRepository.save(communityPost);
    }


    //내 동네 사람들이 올린 커뮤니티 전체글 보는 메소드
    public List<PostResponseDto> communityPostList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        List<CommunityPost> communityPosts = communityPostRepository.findByNeighborhood(user.getNeighborhood());

        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (CommunityPost communityPost : communityPosts) {
            if(communityPost.getStatus() == postStatus.APPROVED) {
                postResponseDtos.add(PostResponseDto.of(communityPost));
            }
        }
        return postResponseDtos;
    }

    //커뮤니티 글에 참여하는 메소드
    public ResponseEntity<?> joinCommunityPost(CommunityPost communityPost, User user, Long number) throws IOException {
        //참여함과 동시에 참여자 테이블에 추가
        Participations participations = new Participations();
        participations.setCommunityPost(communityPost);
        participations.setQuantity(number);
        participations.setUser(user);
        //참여 상태를 JOIN으로
        participations.setStatus(participationStatus.JOIN);
        //공구 참여자와 작성자를 구분
        if(user == communityPost.getUser()){
            participations.setWriter(true);
        }else {
            participations.setWriter(false);
        }
        participationsRepository.save(participations);

        //마지막 참가자인 경우 글의 상태가 결제 대기로 바뀌게 + 결제 데드라인 생성
        if(getParticipantCount(communityPost.getCommunityPostId()).equals(communityPost.getAvailableNumber())){
            communityPost.setStatus(postStatus.PAYMENT_STANDBY);
            communityPost.setPaymentDeadline(LocalDateTime.now().plusHours(12));
        }


        return ResponseEntity.ok(Map.of("message","참여가 완료되었습니다."));
    }

    //참여 수량 세는 메서드
    public Long getParticipantCount(Long communityPostId) {
        List<Participations> joinParticipations = participationsRepository.findAllByCommunityPost_CommunityPostIdAndStatus(communityPostId, participationStatus.JOIN);
        Long count = 0L;
        for(Participations participation : joinParticipations){
            count += participation.getQuantity();
        }
        return count;
    }

    //커뮤니티 글에 참여 후 취소하는 메소드
    public void cancleCommunityPost(CommunityPost communityPost, User user, Participations participations, List<Participations> participationsList) {

        //작성자일 경우 글이 삭제상태로 바뀌면서 이 글에 참여했던 참여자들의 상태도 CANCLE로 바뀜
        if(participations.isWriter()){
            communityPost.setStatus(postStatus.DELETED);
            communityPostRepository.save(communityPost);
            participationsList.forEach(participation -> participation.setStatus(participationStatus.CANCLE));
            participationsRepository.saveAll(participationsList);
        }
        //작성자 아닐경우 참여 정보 취소로 업데이트
        participations.setStatus(participationStatus.CANCLE);
        participationsRepository.save(participations);

    }
}
