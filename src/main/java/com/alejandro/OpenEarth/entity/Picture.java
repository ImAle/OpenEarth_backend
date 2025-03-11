package com.alejandro.OpenEarth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
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

    public Picture(String url, User user) {
        this.url = url;
        this.user = user;
        this.house = null;
    }

    public Picture(String url, House house) {
        this.url = url;
        this.user = null;
        this.house = house;
    }

}
