package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.ChatMember;
import dev_final_team10.GoodBuyUS.domain.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.domain.Post;
import dev_final_team10.GoodBuyUS.repository.ChatMemberRepository;
import dev_final_team10.GoodBuyUS.repository.ChatRoomRepository;
import dev_final_team10.GoodBuyUS.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final PostRepository postRepository;


    public ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO) {
        Post post = postRepository.findById(chatRoomDTO.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));

        if(chatRoomDTO.getCapacity() == post.getCapacity()){
            ChatRoom chatRoom = new ChatRoom("roomName "+chatRoomDTO.getPostId(), post, chatRoomDTO.getCapacity());

            return chatRoomRepository.save(chatRoom);
        }else{
            throw new RuntimeException("Capacity does not match");
        }
    }



}
