package dev_final_team10.GoodBuyUS.controller.api;

import dev_final_team10.GoodBuyUS.domain.community.dto.CommunityCommentDto;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityComment;
import dev_final_team10.GoodBuyUS.service.CommunityCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//커뮤니티 댓글, 대댓글 관련 Controller
@RestController
@AllArgsConstructor
@RequestMapping("/api/community/comment")
public class CommunityCommentController {

    private final CommunityCommentService commentService;
    
    //댓글 작성
    @PostMapping("{community_post_id}")
    public ResponseEntity<?> writeComment(@RequestBody CommunityCommentDto communityCommentDto,
                                          @PathVariable Long community_post_id){
        commentService.writeComment(communityCommentDto, community_post_id);
        return ResponseEntity.ok(Map.of("message", "댓글 작성이 완료되었습니다"));
    }
}
