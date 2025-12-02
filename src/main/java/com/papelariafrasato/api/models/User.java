package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @NotBlank(message = "CPF is mandatory")
    private String cpf;
    @NotBlank(message = "Phone is mandatory")
    private String phone;
    private String role;
    @JsonManagedReference("user-address")
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Address address;
    @JsonManagedReference("user-cart")
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<DeliveryPackage> deliveryPackages;

}
