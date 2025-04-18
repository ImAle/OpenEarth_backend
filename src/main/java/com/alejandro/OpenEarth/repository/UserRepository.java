package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.entity.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRoleNot(UserRole role);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.houses h LEFT JOIN FETCH h.pictures LEFT JOIN FETCH u.rents LEFT JOIN FETCH u.reviews WHERE u.id = :id")
    Optional<User> findFullDetailUser(long id);
}
