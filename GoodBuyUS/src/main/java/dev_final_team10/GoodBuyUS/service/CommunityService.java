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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Value("${file.upload-dir}")
    private String uploadDir;
    //글 작성 메소드
    public void writePost(WriteModifyPostDto writeModifyPostDto, List<MultipartFile> images) throws IOException {
        //현재 사용자 정보 가져오기(글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);
        //현재 사용자 동네 정보 가져오기
        Neighborhood neighborhood = user.getNeighborhood();
        //카테고리 설정
        CommunityCategory communityCategory = CommunityCategory.fromString(writeModifyPostDto.getCategory());
        List<String> postImages =  new ArrayList<String>();
        for(MultipartFile image: images){
            String save = saveImage(image);
            postImages.add(save);

        }
        //DTO -> entity로 변환
        CommunityPost communityPost = writeModifyPostDto.toEntityForCreate(user, neighborhood, communityCategory, postImages);


        //DB 저장
        communityPostRepository.save(communityPost);
    }

    //이미지를 서버에 저장하는 메소드
    private String saveImage(MultipartFile profile) throws IOException {
        if (profile == null || profile.isEmpty()) {
            throw new IOException("이미지를 넣어주세요.");
        }

        // 파일 이름 추출
        String fileName = StringUtils.cleanPath(profile.getOriginalFilename());

        // 파일 저장 경로 설정
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        // 디렉터리 생성 (경로가 없으면 생성)
        Files.createDirectories(targetLocation.getParent());

        // 파일 저장
        profile.transferTo(targetLocation);

        // 저장된 이미지 파일 경로 반환 (URL로 변경 가능)
        return targetLocation.toString();  // 또는 저장된 경로의 URL 반환 가능
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
            List<Participations> participationsList = participationsRepository.findAllByCommunityPost_CommunityPostIdAndStatus(communityPost.getCommunityPostId(), participationStatus.JOIN);
            for (Participations participations1 : participationsList) {
                participations1.setStatus(participationStatus.PAYMENT_STANDBY);
                participationsRepository.save(participations);
            }
            participationsRepository.save(participations);
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
    //결제 수량 세는 메서드
    public Long getPaymentCount(Long communityPostId) {
        List<Participations> paymentParticipations = participationsRepository.findAllByCommunityPost_CommunityPostIdAndStatus(communityPostId, participationStatus.PAYMENT_COMPLETE);
        Long count = 0L;
        for(Participations participation : paymentParticipations){
            count += participation.getQuantity();
        }
        return count;
    }

    //커뮤니티 글에 참여 후 취소하는 메소드
    public void cancleCommunityPost(CommunityPost communityPost, User user, Participations participations, List<Participations> participationsList) {

        //작성자일 경우 글이 삭제상태로 바뀌면서 이 글에 참여했던 참여자들의 상태도 CANCEL로 바뀜
        if(participations.isWriter()){
            communityPost.setStatus(postStatus.DELETED);
            communityPostRepository.save(communityPost);
            participationsList.forEach(participation -> participation.setStatus(participationStatus.CANCEL));
            participationsRepository.saveAll(participationsList);
        }
        //작성자 아닐경우 참여 정보 취소로 업데이트
        participations.setStatus(participationStatus.CANCEL);
        participationsRepository.save(participations);

        if(communityPost.getStatus().equals(postStatus.PAYMENT_STANDBY)){
            //결제 대기 상태인데 취소 (다 찼는데 취소할 경우) 할 경우 기간 남았으면 모집 상태로 기간 끝났으면 우선 DELETED로
            if(communityPost.getCloseAt().isAfter(LocalDateTime.now())){
                communityPost.setStatus(postStatus.APPROVED);
                communityPost.setPaymentDeadline(null);
                communityPostRepository.save(communityPost);
            }
            communityPost.setStatus(postStatus.DELETED);
            communityPost.setPaymentDeadline(null);
            communityPostRepository.save(communityPost);
        }

    }


}
