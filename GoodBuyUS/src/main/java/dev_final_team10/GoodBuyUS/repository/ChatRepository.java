package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.chat.entity.Chat;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoomId(Long chatRoomId);

    void deleteByChatRoom(ChatRoom chatRoom);
}
