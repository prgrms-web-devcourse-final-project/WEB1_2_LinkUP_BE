package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatMember;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    @Query("SELECT c.chatRoom FROM ChatMember c WHERE c.user.id = :userId")
    List<ChatRoom> findChatRoomByUserId(@Param("userId") Long userId);
}
