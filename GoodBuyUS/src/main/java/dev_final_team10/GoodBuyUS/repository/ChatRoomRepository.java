package dev_final_team10.GoodBuyUS.repository;

import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
