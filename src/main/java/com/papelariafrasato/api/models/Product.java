package com.papelariafrasato.api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotBlank(message = "Product name is mandatory")
    private String name;
    @NotBlank(message = "Description is mandatory")
    @Size(max = 255)
    private String description;
    @NotBlank(message = "Price is mandatory")
    @Min(1)
    private Integer price;
    private Integer discount;
    private Integer priceWithDiscount;
    @NotBlank(message = "Category is mandatory")
    private String category;
}
