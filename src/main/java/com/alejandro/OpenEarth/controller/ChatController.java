package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.ChatConversationDto;
import com.alejandro.OpenEarth.dto.ChatMessageDto;
import com.alejandro.OpenEarth.dto.MessageAttachmentDto;
import com.alejandro.OpenEarth.entity.AttachmentType;
import com.alejandro.OpenEarth.service.ChatService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    @Qualifier("chatService")
    private ChatService chatService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Value("${file.upload-dir:uploads/audio}")
    private String uploadDir;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Map<String, Object> payload) {
        Long senderId = Long.valueOf(payload.get("senderId").toString());
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String content = payload.get("content").toString();
        AttachmentType type = AttachmentType.valueOf(payload.get("type").toString());

        MessageAttachmentDto maDto = new MessageAttachmentDto();
        maDto.setType(type);

        chatService.sendMessage(senderId, receiverId, content, List.of(maDto));
    }

    @MessageMapping("/chat.markRead")
    public void markAsRead(@Payload Map<String, Object> payload) {
        Long messageId = Long.valueOf(payload.get("messageId").toString());
        Long userId = Long.valueOf(payload.get("userId").toString());

        chatService.markMessageAsRead(messageId, userId);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<?>> getConversations(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.getUser(token).getId();

        List<ChatConversationDto> conversations = chatService.getConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/messages/{otherUserId}")
    public ResponseEntity<List<?>> getMessages(@RequestHeader("Authorization") String token, @PathVariable Long otherUserId) {
        Long userId = jwtService.getUser(token).getId();

        List<ChatMessageDto> messages = chatService.getMessageHistory(userId, otherUserId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestHeader("Authorization") String token, @RequestBody Map<String, Object> payload) {
        Long senderId = jwtService.getUser(token).getId();
        Long receiverId = Long.valueOf(payload.get("receiverId").toString());

        String content = payload.get("content").toString();

        MessageAttachmentDto maDto = null;

        if(payload.get("type") != null) {
            maDto = new MessageAttachmentDto();
            AttachmentType type = AttachmentType.valueOf(payload.get("type").toString());
            maDto.setType(type);
        }

        ChatMessageDto message;

        if(maDto != null)
            message = chatService.sendMessage(senderId, receiverId, content, List.of(maDto));
        else
            message = chatService.sendMessage(senderId, receiverId, content, List.of());

        return ResponseEntity.ok(message);
    }

    @PostMapping("/send-audio")
    public ResponseEntity<?> sendAudioMessage(@RequestHeader("Authorization") String token,
                                              @RequestParam("receiverId") Long receiverId,
                                              @RequestParam("audioFile") MultipartFile audioFile) throws IOException {

        Long senderId = jwtService.getUser(token).getId();

        ChatMessageDto message = chatService.sendAudioMessage(senderId, receiverId, audioFile);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/audio/{fileName}")
    public ResponseEntity<?> getAudioFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/upload-attachment")
    public ResponseEntity<MessageAttachmentDto> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                                 @RequestParam("type") AttachmentType type) throws IOException {

        MessageAttachmentDto attachment = chatService.uploadAttachment(file, type);
        return ResponseEntity.ok(attachment);
    }
}
