package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.community.dto.CommunityCommentDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.CommunityCommentResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityComment;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.CommunityCommentRepository;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import dev_final_team10.GoodBuyUS.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CommunityCommentService {
    private final UserRepository userRepository;
    private final CommunityPostRepository communityPostRepository;
    private final CommunityCommentRepository communityCommentRepository;

    //댓글 작성
    public CommunityCommentResponseDto writeComment(CommunityCommentDto communityCommentDto, Long postId) {
        //현재 사용자 정보 가져오기(댓글 작성자)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElse(null);

        //해당 포스트 가져오기
        CommunityPost communityPost = communityPostRepository.findById(postId).orElse(null);

        //Dto-> entity변환 후 DB저장
        CommunityComment communityComment = communityCommentDto.toEntityForCreate(communityPost, user, communityCommentDto.getParentId(), communityCommentDto.getContent());
        communityCommentRepository.save(communityComment);

        return CommunityCommentResponseDto.of(communityComment);
    }

    //댓글 수정
    public CommunityCommentResponseDto modifyComment(String content, Long communityCommentId) {
        CommunityComment comment = communityCommentRepository.findById(communityCommentId)  .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. ID: " + communityCommentId));
        comment.update(content);
        return CommunityCommentResponseDto.of(comment);
    }

    //댓글 삭제
    public void deleteComment(Long communityCommentId) {
        communityCommentRepository.deleteById(communityCommentId);
    }
}
