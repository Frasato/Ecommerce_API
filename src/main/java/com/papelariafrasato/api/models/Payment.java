package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String paymentId;
    private String paymentType;
    private String status;
    private Integer changeFor;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
