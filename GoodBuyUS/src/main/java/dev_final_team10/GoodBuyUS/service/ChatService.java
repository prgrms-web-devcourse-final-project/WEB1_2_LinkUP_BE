package dev_final_team10.GoodBuyUS.service;

import dev_final_team10.GoodBuyUS.domain.chat.dto.ChatDTO;
import dev_final_team10.GoodBuyUS.domain.chat.entity.Chat;
import dev_final_team10.GoodBuyUS.domain.chat.entity.ChatRoom;
import dev_final_team10.GoodBuyUS.repository.ChatRepository;
import dev_final_team10.GoodBuyUS.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatDTO> getAllChat(Long chatRoomId){
        List<Chat> chatlist = chatRepository.findByChatRoomId(chatRoomId);
        List<ChatDTO> chatDTOList = new ArrayList<>();
        for(Chat chat : chatlist){
            chatDTOList.add(Chat.toDTO(chat));
        }

        return chatDTOList;
    }

    public ChatDTO createChat(ChatDTO chatDTO, Long chatRoomId){
        ChatRoom chatroom = chatRoomRepository.findById(chatRoomId).get();
        Chat chat = ChatDTO.toEntity(chatDTO, chatroom);

        return Chat.toDTO(chatRepository.save(chat));
    }
}
