package com.alejandro.OpenEarth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private double price;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @OneToOne
    @JoinColumn(name="payment_id", nullable = false)
    private Payment payment;

    public double getTotalPrice(){
        return price * Duration.between(startDate, endDate).toDays();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rent rent)) return false;
        return Objects.equals(id, rent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", user=" + user +
                ", payment=" + payment +
                '}';
    }
}
