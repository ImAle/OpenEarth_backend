package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.ChatConversationDto;
import com.alejandro.OpenEarth.dto.ChatMessageDto;
import com.alejandro.OpenEarth.dto.MessageAttachmentDto;
import com.alejandro.OpenEarth.entity.*;
import com.alejandro.OpenEarth.repository.ChatConversationRepository;
import com.alejandro.OpenEarth.repository.ChatMessageRepository;
import com.alejandro.OpenEarth.repository.MessageAttachmentRepository;
import com.alejandro.OpenEarth.repository.UserRepository;
import com.alejandro.OpenEarth.service.ChatService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

    @Autowired
    @Qualifier("chatMessageRepository")
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    @Qualifier("chatConversationRepository")
    private ChatConversationRepository chatConversationRepository;

    @Autowired
    @Qualifier("messageAttachmentRepository")
    private MessageAttachmentRepository messageAttachmentRepository;

    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Value("${file.upload-dir:uploads/audio}")
    private String uploadDir;

    @Transactional
    public ChatMessageDto sendMessage(Long senderId, Long receiverId, String textContent, List<MessageAttachmentDto> attachments) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTextContent(textContent);
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);

        ChatMessage savedMessage = chatMessageRepository.save(message);

        if (!attachments.isEmpty()) {
            for (MessageAttachmentDto attachmentDTO : attachments) {
                MessageAttachment attachment = new MessageAttachment();
                attachment.setMessage(savedMessage);
                attachment.setType(attachmentDTO.getType());
                attachment.setContent(attachmentDTO.getContent());
                attachment.setMetadata(attachmentDTO.getMetadata());
                messageAttachmentRepository.save(attachment);
            }
        }

        // Update or create a conversation
        ChatConversation conversation = chatConversationRepository
                .findConversationBetweenUsers(sender, receiver)
                .orElse(new ChatConversation());

        if (conversation.getId() == null) {
            conversation.setUser1(sender);
            conversation.setUser2(receiver);
            conversation.getMessages().add(savedMessage);
        } else {
            conversation.getMessages().add(savedMessage);
        }

        conversation.setLastActivity(LocalDateTime.now());
        chatConversationRepository.save(conversation);

        ChatMessageDto messageDTO = convertToDTO(savedMessage);

        // Send message through WebSocket
        messagingTemplate.convertAndSendToUser(
                receiver.getUsername(),
                "/queue/messages",
                messageDTO
        );

        return messageDTO;
    }

    @Transactional
    public ChatMessageDto sendAudioMessage(Long senderId, Long receiverId, MultipartFile audioFile) throws IOException {
        // Creates directory if it does not exist
        Path dirPath = Paths.get(uploadDir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Upload file
        String fileName = UUID.randomUUID().toString() + ".mp3";
        Path filePath = dirPath.resolve(fileName);
        Files.write(filePath, audioFile.getBytes());

        // Create message with file's URL
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        ChatMessage message = new ChatMessage();
        MessageAttachment messageAttachment = new MessageAttachment();

        message.setSender(sender);
        message.setReceiver(receiver);
        message.setTextContent("Audio message");
        message.setTimestamp(LocalDateTime.now());
        message.setRead(false);
        messageAttachment.setType(AttachmentType.AUDIO);

        messageAttachment.setContent("/api/chat/audio/" + fileName);
        messageAttachment.setMetadata(fileName);
        messageAttachment.setMessage(message);
        message.setAttachments(List.of(messageAttachment));

        messageAttachmentRepository.save(messageAttachment);
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Update conversation
        ChatConversation conversation = chatConversationRepository
                .findConversationBetweenUsers(sender, receiver)
                .orElse(new ChatConversation());

        if (conversation.getId() == null) {
            conversation.setUser1(sender);
            conversation.setUser2(receiver);
            conversation.getMessages().add(savedMessage);
        } else {
            conversation.getMessages().add(savedMessage);
        }

        conversation.setLastActivity(LocalDateTime.now());
        chatConversationRepository.save(conversation);

        // Create DTO y send
        ChatMessageDto messageDTO = convertToDTO(savedMessage);
        messagingTemplate.convertAndSendToUser(
                receiver.getRealUsername(),
                "/queue/messages",
                messageDTO
        );

        return messageDTO;
    }

    @Transactional
    public List<ChatMessageDto> getMessageHistory(Long userId, Long otherUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Other user not found"));

        List<ChatMessage> messages = chatMessageRepository.findMessagesBetweenUsers(user, otherUser);

        // mark messages as read
        messages.forEach(message -> {
            if (message.getReceiver().getId().equals(userId) && !message.isRead()) {
                message.setRead(true);
            }
        });
        chatMessageRepository.saveAll(messages);

        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatConversationDto> getConversations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ChatConversation> conversations = chatConversationRepository.findConversationsForUser(user);

        return conversations.stream()
                .map(conversation -> {
                    ChatConversationDto dto = new ChatConversationDto();
                    dto.setId(conversation.getId());
                    dto.setUser1Id(conversation.getUser1().getId());
                    dto.setUser1Username(conversation.getUser1().getRealUsername());
                    dto.setUser2Id(conversation.getUser2().getId());
                    dto.setUser2Username(conversation.getUser2().getRealUsername());
                    dto.setLastActivity(conversation.getLastActivity());

                    // Obtener último mensaje
                    if (!conversation.getMessages().isEmpty()) {
                        ChatMessage lastMessage = conversation.getMessages()
                                .stream()
                                .max((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                                .orElse(null);
                        if (lastMessage != null) {
                            dto.setLastMessage(convertToDTO(lastMessage));
                        }
                    }

                    // Contar mensajes no leídos
                    User otherUser = conversation.getUser1().getId().equals(userId)
                            ? conversation.getUser2() : conversation.getUser1();
                    int unreadCount = (int) conversation.getMessages().stream()
                            .filter(m -> m.getReceiver().getId().equals(userId) && !m.isRead())
                            .count();
                    dto.setUnreadCount(unreadCount);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void markMessageAsRead(Long messageId, Long userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (message.getReceiver().getId().equals(userId)) {
            message.setRead(true);
            chatMessageRepository.save(message);
        }
    }

    // Method to upload files (images, audios, etc)
    @Transactional
    public MessageAttachmentDto uploadAttachment(MultipartFile file, AttachmentType type) throws IOException {
        // Create repository if it does not exists
        String uploadSubdir = type.name().toLowerCase() + "s"; // images, audios, etc.
        Path dirPath = Paths.get(uploadDir + "/" + uploadSubdir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Upload file
        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null ?
                originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
        String fileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = dirPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // Create DTO
        MessageAttachmentDto attachmentDTO = new MessageAttachmentDto();
        attachmentDTO.setType(type);
        attachmentDTO.setContent("/api/chat/" + uploadSubdir + "/" + fileName);
        attachmentDTO.setMetadata(originalFileName);

        return attachmentDTO;
    }

    private ChatMessageDto convertToDTO(ChatMessage message) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getRealUsername());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverUsername(message.getReceiver().getRealUsername());
        dto.setTextContent(message.getTextContent());
        dto.setTimestamp(message.getTimestamp());
        dto.setRead(message.isRead());

        // Transform attachments
        List<MessageAttachmentDto> attachmentDTOs = message.getAttachments().stream()
                .map(attachment -> {
                    MessageAttachmentDto attachmentDTO = new MessageAttachmentDto();
                    attachmentDTO.setId(attachment.getId());
                    attachmentDTO.setType(attachment.getType());
                    attachmentDTO.setContent(attachment.getContent());
                    attachmentDTO.setMetadata(attachment.getMetadata());
                    return attachmentDTO;
                })
                .collect(Collectors.toList());

        dto.setAttachments(attachmentDTOs);

        return dto;
    }
}
