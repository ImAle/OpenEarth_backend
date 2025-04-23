package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.AttachmentType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageAttachmentDto {
    private Long id;
    private AttachmentType type;
    private String content;
    private String metadata;
}
