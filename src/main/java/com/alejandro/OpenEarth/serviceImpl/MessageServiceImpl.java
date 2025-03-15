package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.dto.MessageDto;
import com.alejandro.OpenEarth.entity.Message;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.repository.MessageRepository;
import com.alejandro.OpenEarth.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Autowired
    @Qualifier("messageRepository")
    private MessageRepository messageRepository;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Override
    public Message saveMessage(MessageDto messageDto) {
        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setTimestamp(messageDto.getTimestamp());
        User sender = userService.getUserById(messageDto.getSenderId());
        User receiver = userService.getUserById(messageDto.getReceiverId());
        message.setSender(sender);
        message.setReceiver(receiver);

        return messageRepository.save(message);
    }

    @Override
    public Long getReceiverId(String token) {
        return jwtService.getUser(token).getId();
    }

    @Override
    public List<Message> getChatHistory(Long senderId, Long receiverId) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(senderId, receiverId, receiverId, senderId);
    }
}
