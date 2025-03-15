package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.dto.MessageDto;
import com.alejandro.OpenEarth.entity.Message;

import java.util.List;

public interface MessageService {

    Message saveMessage(MessageDto messageDto);
    Long getReceiverId(String token);
    List<Message> getChatHistory(Long senderId, Long receiverId);

}
