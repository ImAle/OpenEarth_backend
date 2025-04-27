package com.alejandro.OpenEarth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private int guests;
    @Column(nullable = false)
    private int bedrooms;
    @Column(nullable = false)
    private int beds;
    @Column(nullable = false)
    private int bathrooms;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String location;
    private double latitude;
    private double longitude;
    @Enumerated(EnumType.STRING)
    private HouseCategory category;
    @Enumerated(EnumType.STRING)
    private HouseStatus status;
    private LocalDate creationDate;
    private LocalDate lastUpdateDate;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Picture> pictures;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Rent> rents = new HashSet<>();

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof House house)) return false;
        return Double.compare(latitude, house.latitude) == 0 && Double.compare(longitude, house.longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}

