package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatConversationDto {
    private Long id;
    private Long user1Id;
    private String user1Username;
    private Long user2Id;
    private String user2Username;
    private LocalDateTime lastActivity;
    private ChatMessageDto lastMessage;
    private int unreadCount;
}
