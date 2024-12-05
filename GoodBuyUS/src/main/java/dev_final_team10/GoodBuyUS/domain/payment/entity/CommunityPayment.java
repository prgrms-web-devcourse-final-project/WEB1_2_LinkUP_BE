package dev_final_team10.GoodBuyUS.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "community_payment")
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long communityPaymentId;

    @Column(nullable = false, unique = true)
    private String participationsOrderId;

    @Column(nullable = true)
    private String communityPaymentKey;

    @Column(nullable = true)
    private String secret;

    @Column(nullable = true)
    private String bankId;

    @Column(nullable = true)
    private String accountNumber;

    @Column(nullable = true)
    private String customerName;

    /*@Column(nullable = true)
    private LocalDateTime dueDate;*/

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String paymentStatus;

    @Column(nullable = false)
    private LocalDateTime communityCreatedAt;

    @Column(nullable = true)
    private LocalDateTime communityApprovedAt;

    @Column(nullable = true)
    private Integer refundedAmount;

    @Column(nullable = true)
    private LocalDateTime canceledAt;

    @Column(nullable = true)
    private String cancelReason;

    @Column(nullable = true)
    private String qrCode;

    @Column(nullable = true)
    private Boolean isQrGenerated;

    @Column(nullable = true)
    private String qrStatus;
}

