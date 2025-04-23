package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long receiverId;
    private String receiverUsername;
    private String textContent;
    private LocalDateTime timestamp;
    private boolean read;
    private List<MessageAttachmentDto> attachments = new ArrayList<>();
}
