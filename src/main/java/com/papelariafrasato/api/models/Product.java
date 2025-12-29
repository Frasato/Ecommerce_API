package com.papelariafrasato.api.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    @NotBlank(message = "Height is mandatory")
    private String height;
    @NotBlank(message = "Width is mandatory")
    private String width;
    @NotBlank(message = "Product Length is mandatory")
    private String product_length;
    @NotBlank(message = "Weight is mandatory")
    private String weight;
    @NotBlank(message = "Producer is mandatory")
    private String producer;
    @NotNull
    @Min(100)
    private Integer price;
    private Integer discount;
    private LocalDate discountEnd;
    private LocalDate discountInit;
    private Integer priceWithDiscount;
    @NotBlank(message = "Category is mandatory")
    private String category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @JsonBackReference
    @OneToOne(mappedBy = "product")
    private ProductAnalytics productAnalytics;
}
