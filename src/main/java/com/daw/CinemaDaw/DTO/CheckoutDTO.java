package com.daw.CinemaDaw.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CheckoutDTO {

    @NotBlank(message = "Name is required")
    private String clientName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is not valid")
    private String clientEmail;

    private String couponCode;

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getClientEmail() { return clientEmail; }
    public void setClientEmail(String clientEmail) { this.clientEmail = clientEmail; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
}
