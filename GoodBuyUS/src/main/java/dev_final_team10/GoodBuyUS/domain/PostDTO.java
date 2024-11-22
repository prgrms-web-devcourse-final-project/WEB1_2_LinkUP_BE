package dev_final_team10.GoodBuyUS.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private String title;
    private int capacity;

    public PostDTO(Post post) {
        this.title = post.getTitle();
        this.capacity = post.getCapacity();
    }

    public Post toEntity(){
        Post post = Post.builder()
                .title(title)
                .capacity(capacity)
                .build();
        return post;
    }
}

