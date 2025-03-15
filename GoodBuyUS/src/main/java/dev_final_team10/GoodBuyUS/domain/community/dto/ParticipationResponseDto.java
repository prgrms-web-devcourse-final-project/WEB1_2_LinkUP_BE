package dev_final_team10.GoodBuyUS.domain.community.dto;

import dev_final_team10.GoodBuyUS.domain.community.entity.Participations;
import dev_final_team10.GoodBuyUS.domain.community.entity.participationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class ParticipationResponseDto {
    private Long participationId;
    private boolean isWriter;
    private Long quantity;
    private participationStatus status;
    private Long communutyPostId;
    private Long userId;


    public static ParticipationResponseDto of(Participations participations){
        ParticipationResponseDto participationResponseDto = new ParticipationResponseDto();
        participationResponseDto.setParticipationId(participations.getParticipationId());
        participationResponseDto.setQuantity(participations.getQuantity());
        participationResponseDto.setStatus(participations.getStatus());
        participationResponseDto.setWriter(participations.isWriter());
        participationResponseDto.setCommunutyPostId(participationResponseDto.communutyPostId);
        participationResponseDto.setUserId(participationResponseDto.userId);
        return participationResponseDto;
    }
}
