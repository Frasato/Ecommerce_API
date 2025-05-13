package com.papelariafrasato.api.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String street;
    private int number;
    private String city;
    private int CEP;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
