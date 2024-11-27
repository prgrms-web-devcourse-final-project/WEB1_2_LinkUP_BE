package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.chat.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.chat.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.domain.Post;
import dev_final_team10.GoodBuyUS.repository.ChatMemberRepository;
import dev_final_team10.GoodBuyUS.repository.ChatRoomRepository;
import dev_final_team10.GoodBuyUS.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final PostRepository postRepository;

    //채팅방 생성
    public ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO) {
        Post post = postRepository.findById(chatRoomDTO.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));

        ChatRoom chatRoom = chatRoomDTO.toEntity(post);
        if(chatRoomDTO.getCapacity() == post.getCapacity()){
            return chatRoomRepository.save(chatRoom);
        }else{
            throw new RuntimeException("Capacity does not match");
        }
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
