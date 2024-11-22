package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
//
//    @Query("SELECT COUNT(c) FROM ChatMember c WHERE c.post.id = :postId AND c.isPaid = true")
//    int countPaidMembersByChatRoomAndPost(@Param("postId") Long postId);
}
