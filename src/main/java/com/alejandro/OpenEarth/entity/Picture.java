package com.alejandro.OpenEarth.entity;

import jakarta.persistence.*;

@Entity
public class Picture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name= "house_id")
    private House house;

    @OneToOne
    @JoinColumn(name="user_id", unique = true)
    private User user;
}
