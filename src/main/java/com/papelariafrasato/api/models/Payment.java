package com.papelariafrasato.api.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;
    private String paymentName;
    private double value;
    private String method;
    private Instant paymentDate;

    @PrePersist
    public void setPaymentDate(){
        this.paymentDate = Instant.now();
    }

    public Payment() {
    }

    public Payment(String paymentId, String paymentName, double value, String method, Instant paymentDate) {
        this.paymentId = paymentId;
        this.paymentName = paymentName;
        this.value = value;
        this.method = method;
        this.paymentDate = paymentDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }
}
