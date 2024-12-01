package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.repository.ChatMemberRepository;
import dev_final_team10.GoodBuyUS.repository.ChatRoomRepository;
import dev_final_team10.GoodBuyUS.repository.CommunityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final CommunityPostRepository postRepository;

    //채팅방 생성
    public ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO) {
        CommunityPost post = postRepository.findById(chatRoomDTO.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));

        ChatRoom chatRoom = chatRoomDTO.toEntity(post);
        return chatRoomRepository.save(chatRoom);
    }

    //채팅방 삭제
    public void removeChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        chatRoomRepository.delete(chatRoom);
    }

    //채팅방 목록 조회
    public List<ChatRoomDTO> getListChatRoom(Long userId) {
        List<ChatRoom> chatRoom = chatMemberRepository.findChatRoomByUserId(userId);

        return chatRoom.stream()
                .map(room -> new ChatRoomDTO(room.getId(), room.getCapacity(), room.getRoomName()))
                .collect(Collectors.toList());
    }
}
