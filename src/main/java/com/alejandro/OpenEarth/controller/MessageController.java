package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.MessageDto;
import com.alejandro.OpenEarth.entity.Message;
import com.alejandro.OpenEarth.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    @Qualifier("messageService")
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto message, @RequestHeader("Authorization") String token){
        Long senderId = messageService.getReceiverId(token);
        message.setSenderId(senderId);

        Message m = messageService.saveMessage(message);

        messagingTemplate.convertAndSendToUser(message.getReceiverId().toString(), "/queue/messages", m);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(@RequestHeader("Authorization") String token, @RequestParam Long receiverId){
        Long senderId = messageService.getReceiverId(token);
        List<Message> messages = messageService.getChatHistory(senderId, receiverId);
        return ResponseEntity.ok().body(Map.of("messages", messages));
    }
}
