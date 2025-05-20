package com.papelariafrasato.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Email is mandatory")
    private String email;
    private String customerId;
    @NotBlank(message = "CPF is mandatory")
    private String cpf;
    private String role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
    @OneToMany(mappedBy = "user")
    private List<Payment> payment;

}
