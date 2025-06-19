package com.bkap.respository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bkap.entity.Order;
import com.bkap.entity.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);
	List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
    Optional<Order> findByTransactionId(String transactionId);
    List<Order> findByPaymentStatus(String paymentStatus);
}

