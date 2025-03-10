package com.alejandro.OpenEarth.entity;

import com.alejandro.OpenEarth.dto.HouseCreationDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
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
    @Column(nullable = false)
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
    private String coordinates;
    private HouseCategory category;
    private HouseStatus status;
    private LocalDate creationDate;
    private LocalDate lastUpdateDate;

    @OneToMany(mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Picture> pictures;

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
        return guests == house.guests && bedrooms == house.bedrooms && beds == house.beds && bathrooms == house.bathrooms && Objects.equals(location, house.location) && Objects.equals(owner, house.owner) && Objects.equals(rents, house.rents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guests, bedrooms, beds, bathrooms, location, owner, rents);
    }
}

