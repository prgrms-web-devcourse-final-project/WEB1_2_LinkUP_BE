package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.community.dto.UserReviewDto;
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
        Participations reviewer = participationsRepository.findById(userReviewDto.getReviewerId()).orElse(null);
        User host = userRepository.findById(userReviewDto.getHostId()).orElse(null);

        UserReview userReview = userReviewDto.toEntityForCreate(userReviewDto, host, reviewer);
        userReviewRepository.save(userReview);

        return userReviewDto;
    }

    //사용자 리뷰 등록

}
