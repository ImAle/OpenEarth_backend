package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.houses h LEFT JOIN FETCH h.pictures LEFT JOIN FETCH u.rents LEFT JOIN FETCH u.reviews WHERE u.id = :id")
    Optional<User> findFullDetailUser(long id);
}
