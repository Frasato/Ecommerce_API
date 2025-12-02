package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice = 0;
    @JsonBackReference("user-cart")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @JsonManagedReference("cart-items")
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItem;

}
