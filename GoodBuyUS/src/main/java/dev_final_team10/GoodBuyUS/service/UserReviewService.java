package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.community.dto.UserReviewDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.UserReviewResponse;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.UserReview;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.ParticipationsRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import dev_final_team10.GoodBuyUS.repository.UserReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
@Log4j2
public class UserReviewService {
    private final ParticipationsRepository participationsRepository;
    private final UserRepository userRepository;
    private final UserReviewRepository userReviewRepository;

    //사용자 리뷰 등록
    public UserReviewDto writeUserReview(UserReviewDto userReviewDto) {
        boolean exists = userReviewRepository.existsByReviewer_ParticipationIdAndHost_Id(
                userReviewDto.getReviewerId(), userReviewDto.getHostId()
        );

        if (exists) {
            throw new IllegalStateException("이미 이 사용자에 대한 리뷰를 작성하였습니다.");
        }

        Participations reviewer = participationsRepository.findById(userReviewDto.getReviewerId()).orElse(null);
        User host = userRepository.findById(userReviewDto.getHostId()).orElse(null);

        UserReview userReview = userReviewDto.toEntityForCreate(userReviewDto, host, reviewer);
        userReviewRepository.save(userReview);

        return userReviewDto;
    }

    //사용자 리뷰 보기
    public UserReviewResponse getUserReviews(Long userId) {

        //리뷰 평점 평균 불러오기
        Double overallAvgScore = userReviewRepository.findOverallAverageScore(userId);
        double avgRating = (overallAvgScore != null) ? overallAvgScore : 0.0;

        //상세 리뷰 목록 불러오기
        List<UserReview> reviews = userReviewRepository.findByHostId(userId);

        List<UserReviewResponse.UserReviewDTO> reviewDTOs = reviews.stream()
                .map(review -> new UserReviewResponse.UserReviewDTO(
                        review.getReviewer().getUser().getId(),
                        review.getReviewer().getUser().getNickname(),
                        review.getContent()
                ))
                .collect(Collectors.toList());

        return new UserReviewResponse(userId, overallAvgScore, reviewDTOs);
    }
}
