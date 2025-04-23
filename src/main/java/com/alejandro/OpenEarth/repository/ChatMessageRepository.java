package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.ChatMessage;
import com.alejandro.OpenEarth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("chatMessageRepository")
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderAndReceiverOrderByTimestampDesc(User sender, User receiver);

    List<ChatMessage> findByReceiverAndReadIsFalse(User receiver);

    @Query("SELECT m FROM ChatMessage m WHERE (m.sender = ?1 AND m.receiver = ?2) OR (m.sender = ?2 AND m.receiver = ?1) ORDER BY m.timestamp")
    List<ChatMessage> findMessagesBetweenUsers(User user1, User user2);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver = ?1 AND m.read = false")
    int countUnreadMessagesForUser(User user);
}
