package com.alejandro.OpenEarth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MessageDto {

    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime timestamp;

}
