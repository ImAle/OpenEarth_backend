package com.alejandro.OpenEarth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentId;
    private String payerId;
    private long userId;
    private String method;
    private String status;
    private String currency;
    private String description;
    private Double amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
