package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @JsonBackReference("cart-items")
    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
}
