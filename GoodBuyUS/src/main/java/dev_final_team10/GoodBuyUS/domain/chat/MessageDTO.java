package dev_final_team10.GoodBuyUS.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private String roomId;
    private String userName;
    private String message;
    private String time;
}
