package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
