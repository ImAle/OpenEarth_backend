package com.alejandro.OpenEarth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @ManyToOne
    @JoinColumn(name="reporter_id", nullable = false)
    private User reporter;

    @ManyToOne
    @JoinColumn(name="reported_id", nullable = false)
    private User reported;

    private LocalDateTime createdAt;
    private boolean deleted;
}
