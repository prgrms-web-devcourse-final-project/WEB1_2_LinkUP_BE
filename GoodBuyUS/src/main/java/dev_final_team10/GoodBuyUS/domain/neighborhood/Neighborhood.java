package dev_final_team10.GoodBuyUS.domain.neighborhood;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Neighborhood {

    @Id
    private Double neighborhoodCode;    //지역 코드
    private String neighborhoodName;    //지역 이름
}
