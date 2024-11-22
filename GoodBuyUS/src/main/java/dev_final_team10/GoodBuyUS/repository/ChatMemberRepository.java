package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.ChatMember;
import dev_final_team10.GoodBuyUS.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    List<ChatMember> findByPostAndIsPaid(Post post, boolean isPaid);
}
