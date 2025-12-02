package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Street is mandatory")
    private String street;
    @NotBlank(message = "District is mandatory")
    private String district;
    @NotBlank(message = "State is mandatory")
    private String countryState;
    @NotBlank(message = "Number is mandatory")
    private String number;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "CEP is mandatory")
    private String CEP;
    @JsonBackReference("user-address")
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
