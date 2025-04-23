package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.ChatConversationDto;
import com.alejandro.OpenEarth.dto.ChatMessageDto;
import com.alejandro.OpenEarth.dto.MessageAttachmentDto;
import com.alejandro.OpenEarth.entity.AttachmentType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChatService {
    ChatMessageDto sendMessage(Long senderId, Long receiverId, String textContent, List<MessageAttachmentDto> attachments);
    ChatMessageDto sendAudioMessage(Long senderId, Long receiverId, MultipartFile audioFile) throws IOException;
    List<ChatMessageDto> getMessageHistory(Long userId, Long otherUserId);
    List<ChatConversationDto> getConversations(Long userId);
    void markMessageAsRead(Long messageId, Long userId);
    MessageAttachmentDto uploadAttachment(MultipartFile file, AttachmentType type) throws IOException;
}
