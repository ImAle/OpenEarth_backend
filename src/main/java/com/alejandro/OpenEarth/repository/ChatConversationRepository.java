package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.ChatConversation;
import com.alejandro.OpenEarth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("chatConversationRepository")
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    @Query("SELECT c FROM ChatConversation c WHERE (c.user1 = ?1 AND c.user2 = ?2) OR (c.user1 = ?2 AND c.user2 = ?1)")
    Optional<ChatConversation> findConversationBetweenUsers(User sender, User receiver);

    @Query("SELECT c FROM ChatConversation c WHERE c.user1 = ?1 OR c.user2 = ?1")
    List<ChatConversation> findConversationsForUser(User user);
}
