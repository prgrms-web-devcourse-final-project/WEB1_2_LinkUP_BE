package dev_final_team10.GoodBuyUS.domain.chat.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev_final_team10.GoodBuyUS.domain.community.entity.CommunityPost;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatMember> members = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private CommunityPost post;

    @CreatedDate
    private LocalDateTime createdAt;

    private int capacity;

}
