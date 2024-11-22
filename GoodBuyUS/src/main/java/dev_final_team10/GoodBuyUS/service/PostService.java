package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.Post;
import dev_final_team10.GoodBuyUS.domain.PostDTO;
import dev_final_team10.GoodBuyUS.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class PostService {
    private final PostRepository postRepository;

    public PostDTO createPost(PostDTO postDTO) {
        Post post = postDTO.toEntity();
        postRepository.save(post);
        return new PostDTO(post);
    }
}
