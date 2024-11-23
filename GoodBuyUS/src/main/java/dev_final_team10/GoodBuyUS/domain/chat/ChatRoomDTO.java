package dev_final_team10.GoodBuyUS.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private Long postId;
    private int capacity;
    private String roomName;

}
