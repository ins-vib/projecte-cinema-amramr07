package com.daw.CinemaDaw.service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.daw.CinemaDaw.domain.order.Coupon;
import com.daw.CinemaDaw.domain.order.Order;
import com.daw.CinemaDaw.repository.CouponRepository;

@Service
public class CouponService {

    public static final double REWARD_THRESHOLD = 25.0;
    public static final double REWARD_DISCOUNT = 5.0;
    public static final double COUPON_MINIMUM_ORDER = 20.0;
    private static final int COUPON_VALID_DAYS = 30;

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponValidationResult validateCoupon(String rawCode, String clientEmail, double subtotalAmount) {
        String normalizedCode = normalizeCode(rawCode);
        if (normalizedCode == null) {
            return CouponValidationResult.empty();
        }

        Coupon coupon = couponRepository.findByCode(normalizedCode)
            .orElseThrow(() -> new IllegalArgumentException("The coupon does not exist."));

        if (coupon.isUsed()) {
            throw new IllegalArgumentException("This coupon has already been used.");
        }
        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("This coupon has expired.");
        }
        if (!coupon.getAssignedEmail().equalsIgnoreCase(clientEmail)) {
            throw new IllegalArgumentException("This coupon is linked to a different email.");
        }
        if (subtotalAmount < coupon.getMinOrderAmount()) {
            throw new IllegalArgumentException(
                "You must reach " + formatAmount(coupon.getMinOrderAmount()) + " € to use this coupon."
            );
        }

        return CouponValidationResult.of(coupon);
    }

    public Coupon createRewardCoupon(Order order) {
        Coupon coupon = new Coupon();
        coupon.setCode(generateUniqueCode());
        coupon.setDiscountAmount(REWARD_DISCOUNT);
        coupon.setMinOrderAmount(COUPON_MINIMUM_ORDER);
        coupon.setAssignedEmail(order.getClientEmail());
        coupon.setExpiryDate(LocalDateTime.now().plusDays(COUPON_VALID_DAYS));
        coupon.setGeneratedByOrderId(order.getId());
        return couponRepository.save(coupon);
    }

    public void markAsUsed(Coupon coupon, Order order) {
        coupon.setUsed(true);
        coupon.setUsedByOrderId(order.getId());
        couponRepository.save(coupon);
    }

    public String normalizeCode(String rawCode) {
        if (rawCode == null) {
            return null;
        }

        String normalized = rawCode.trim().toUpperCase(Locale.ROOT);
        return normalized.isBlank() ? null : normalized;
    }

    private String generateUniqueCode() {
        String code;
        do {
            String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
            code = "CINEX5-" + suffix;
        } while (couponRepository.findByCode(code).isPresent());
        return code;
    }

    private String formatAmount(double amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    public static class CouponValidationResult {
        private final Coupon coupon;
        private final double discountAmount;

        private CouponValidationResult(Coupon coupon, double discountAmount) {
            this.coupon = coupon;
            this.discountAmount = discountAmount;
        }

        public static CouponValidationResult empty() {
            return new CouponValidationResult(null, 0.0);
        }

        public static CouponValidationResult of(Coupon coupon) {
            return new CouponValidationResult(coupon, coupon.getDiscountAmount());
        }

        public Coupon getCoupon() {
            return coupon;
        }

        public double getDiscountAmount() {
            return discountAmount;
        }

        public boolean hasCoupon() {
            return coupon != null;
        }
    }
}
