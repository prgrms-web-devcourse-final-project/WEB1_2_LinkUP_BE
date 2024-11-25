package dev_final_team10.GoodBuyUS.domain.neighborhood.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Neighborhood {

    @Id
    private Double code;
    private String areaName;
}
