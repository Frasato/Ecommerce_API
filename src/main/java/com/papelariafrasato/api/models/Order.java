package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String status;
    private Integer totalPrice;
    @JsonBackReference
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;
    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @OneToOne(mappedBy = "order")
    private DeliveryPackage deliveryPackage;
    @JsonManagedReference
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

}
