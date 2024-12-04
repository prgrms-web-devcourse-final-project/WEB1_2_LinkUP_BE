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
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseEntity<?> joinCommunityPost(CommunityPost communityPost, User user, Long number) {
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
        return ResponseEntity.ok(Map.of("message","참여가 완료되었습니다."));
    }
}
