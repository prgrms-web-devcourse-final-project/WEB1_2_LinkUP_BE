package dev_final_team10.GoodBuyUS.controller;

import dev_final_team10.GoodBuyUS.domain.Post;
import dev_final_team10.GoodBuyUS.domain.PostDTO;
import dev_final_team10.GoodBuyUS.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping()
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        return ResponseEntity.ok(postService.createPost(postDTO));
    }

}