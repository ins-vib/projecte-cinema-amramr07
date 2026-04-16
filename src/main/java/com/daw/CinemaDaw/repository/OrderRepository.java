package com.daw.CinemaDaw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientEmail(String clientEmail);
}