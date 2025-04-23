package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.MessageAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("messageAttachmentRepository")
public interface MessageAttachmentRepository extends JpaRepository<MessageAttachment, Long> {
    List<MessageAttachment> findByMessageId(Long messageId);
}
