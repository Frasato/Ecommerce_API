package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "delivery_package")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Double height;
    private Double width;
    private Double length;
    private Double weight;
    private Integer deliveryPrice;
    private Timestamp createdAt;
    private String deliveryTicketKey;
    private String status;
    private String service;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

}