package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatMemberDTO;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatMember;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatRoomDTO;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.postStatus;
import dev_final_team10.GoodBuyUS.domain.payment.entity.CommunityPayment;
import dev_final_team10.GoodBuyUS.domain.user.entity.User;
import dev_final_team10.GoodBuyUS.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final ChatRepository chatRepository;
    private final CommunityPostRepository postRepository;
    private final ParticipationsRepository participationsRepository;

    //채팅방 생성
    public ChatRoom createChatRoom(ChatRoomDTO chatRoomDTO) {
        CommunityPost post = postRepository.findById(chatRoomDTO.getPostId()).orElseThrow(() -> new RuntimeException("Post not found"));

        //채팅방 멤버 추가
        List<Participations> participations = participationsRepository.findByCommunityPost(post);
        chatRoomDTO.setCapacity(participations.size());

        ChatRoom chatRoom = chatRoomDTO.toEntity(post);
        for (Participations participation : participations) {
            User user = participation.getUser();
            ChatMember chatMember = ChatMemberDTO.toEntity(post, chatRoom, user);
            chatMemberRepository.save(chatMember);
        }

        return chatRoomRepository.save(chatRoom);
    }

    //채팅방 삭제
    @PreAuthorize("hasRole('ADMIN')")
    public void removeChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RuntimeException("ChatRoom not found"));
        chatRepository.deleteByChatRoom(chatRoom);
        chatRoomRepository.delete(chatRoom);
    }

    //내 채팅방 목록 조회
    public List<ChatRoomDTO> getListChatRoom(Long userId) {
        List<ChatRoom> chatRoom = chatMemberRepository.findChatRoomByUserId(userId);

        return chatRoom.stream()
                .map(room -> new ChatRoomDTO(room.getId(), room.getCapacity(), room.getRoomName(),
                        room.getMembers().stream().map(chatMember -> chatMember.getUser().getNickname())
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    //채팅방 전체 조회 - 관리자
    @PreAuthorize("hasRole('ADMIN')")
    public List<ChatRoomDTO> getListAllChatRoom() {
        List<ChatRoom> chatRoom = chatRoomRepository.findAll();

        return chatRoom.stream()
                .map(room -> new ChatRoomDTO(room.getId(), room.getCapacity(), room.getRoomName(),
                        room.getMembers().stream().map(chatMember -> chatMember.getUser().getNickname())
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

}
