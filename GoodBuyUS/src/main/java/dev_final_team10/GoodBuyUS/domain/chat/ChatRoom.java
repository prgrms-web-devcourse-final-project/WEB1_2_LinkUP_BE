package dev_final_team10.GoodBuyUS.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_room")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMember> members = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    private LocalDateTime createdAt;

    private int capacity;

    public ChatRoom(String roomName, Post post, int capacity) {
        this.roomName = roomName;
        this.post = post;
        this.capacity = capacity;
        this.createdAt = LocalDateTime.now();

    }
}
