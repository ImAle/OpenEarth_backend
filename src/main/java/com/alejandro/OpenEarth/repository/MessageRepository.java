package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("messageRepository")
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(Long senderId, Long receiverId, Long receiverId2, Long senderId2);

}
