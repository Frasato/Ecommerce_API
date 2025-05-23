package com.papelariafrasato.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank(message = "Image is mandatory")
    private String image;
    @NotBlank(message = "BarCode is mandatory")
    private String barCode;
    @NotBlank(message = "Product name is mandatory")
    private String name;
    @NotBlank(message = "Description is mandatory")
    @Size(max = 255, min = 25, message = "Enter a description among 25 characters and 255 characters")
    private String description;
    @NotBlank(message = "Producer is mandatory")
    private String producer;
    @NotNull
    @Min(100)
    private Integer price;
    private Integer discount;
    private Integer priceWithDiscount;
    @NotBlank(message = "Category is mandatory")
    private String category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
