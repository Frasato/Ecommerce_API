package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "products_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAnalytics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Integer purchase = 0;
    private Integer click = 0;
    private Integer cartAdded = 0;
    private Instant dateTime;
    @JsonManagedReference
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @PrePersist
    private void setTime(){
        this.dateTime = Instant.now();
    }

}
