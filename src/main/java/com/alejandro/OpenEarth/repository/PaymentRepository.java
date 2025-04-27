package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("paymentRepository")
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByPaymentIdAndStatusIgnoreCase(String paymentId, String status);
}
