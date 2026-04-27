package com.daw.CinemaDaw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.CinemaDaw.domain.order.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
}
