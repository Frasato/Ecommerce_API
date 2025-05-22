package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
