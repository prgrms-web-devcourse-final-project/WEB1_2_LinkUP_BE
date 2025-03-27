package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.dto.CommunityCommentDto;
import dev_final_team10.GoodBuyUS.domain.community.dto.CommunityCommentResponseDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityComment;
import dev_final_team10.GoodBuyUS.service.CommunityCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//커뮤니티 댓글, 대댓글 관련 Controller
@RestController
@AllArgsConstructor
@RequestMapping("/community/comment")
public class CommunityCommentController {

    private final CommunityCommentService commentService;
    
    //댓글 작성
    @PostMapping("{community_post_id}")
    public CommunityCommentResponseDto writeComment(@RequestBody CommunityCommentDto communityCommentDto,
                                                                    @PathVariable Long community_post_id){
         return commentService.writeComment(communityCommentDto, community_post_id);
    }

    //댓글 수정
    @PutMapping("{community_comment_id}")
    public CommunityCommentResponseDto modifyComment(@RequestBody Map<String, String> requestBody,
                                                     @PathVariable Long community_comment_id){
        return commentService.modifyComment(requestBody.get("content"), community_comment_id);
    }

    //댓글 삭제
    @DeleteMapping("{community_comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long community_comment_id){
        commentService.deleteComment(community_comment_id);
        return new ResponseEntity<>(Map.of("message", "댓글이 삭제되었습니다."), HttpStatus.OK);

    }
}
