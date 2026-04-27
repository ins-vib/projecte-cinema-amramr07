package com.daw.CinemaDaw.domain.order;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "discount_amount", nullable = false)
    private double discountAmount;

    @Column(name = "min_order_amount", nullable = false)
    private double minOrderAmount;

    @Column(name = "assigned_email", nullable = false)
    private String assignedEmail;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    @Column(name = "generated_by_order_id")
    private Long generatedByOrderId;

    @Column(name = "used_by_order_id")
    private Long usedByOrderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public String getAssignedEmail() {
        return assignedEmail;
    }

    public void setAssignedEmail(String assignedEmail) {
        this.assignedEmail = assignedEmail;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Long getGeneratedByOrderId() {
        return generatedByOrderId;
    }

    public void setGeneratedByOrderId(Long generatedByOrderId) {
        this.generatedByOrderId = generatedByOrderId;
    }

    public Long getUsedByOrderId() {
        return usedByOrderId;
    }

    public void setUsedByOrderId(Long usedByOrderId) {
        this.usedByOrderId = usedByOrderId;
    }
}
