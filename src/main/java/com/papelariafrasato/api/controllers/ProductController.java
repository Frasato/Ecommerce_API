package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestAddDiscountProductDto;
import com.papelariafrasato.api.dtos.RequestCategoryDiscountDto;
import com.papelariafrasato.api.dtos.ResponseAllProductsDto;
import com.papelariafrasato.api.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/product")
@Tag(
        name = "Product",
        description = "EndPoints to create, get and actualize products"
)
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping()
    @Operation(
            summary = "All Products",
            description = "Get all products"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Get all", content = @Content(schema = @Schema(implementation = ResponseAllProductsDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> getAllProducts(){
        return productService.allProducts();
    }

    @PostMapping()
    @Operation(
            summary = "Create Product",
            description = "Create new product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created product"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> createNewProduct(
            @RequestParam("file") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("barcode") String barcode,
            @RequestParam("desc") String description,
            @RequestParam("producer") String producer,
            @RequestParam("price") Integer price,
            @RequestParam("category") String category
    ){
        return productService.addProduct(
                image,
                barcode,
                name,
                description,
                producer,
                price,
                category
        );
    }

    @PutMapping()
    @Operation(
            summary = "Add discount",
            description = "Add discount on a single product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Added Discount"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> addDiscount(@RequestBody RequestAddDiscountProductDto discountProductDto){
        return productService.addDiscountOnProduct(discountProductDto.productId(), discountProductDto.discount());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Remove discount",
            description = "Remove discount on a single product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Removed Discount"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> removeDiscount(@PathVariable("id") String productId){
        return productService.removeProductDiscount(productId);
    }

    @PutMapping("/category/discount")
    @Operation(
            summary = "Add discount Category",
            description = "Add discount on all products in a category"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Added Discount"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> addDiscountByCategory(@RequestBody RequestCategoryDiscountDto categoryDiscountDto){
        return productService.addDiscountOnCategory(categoryDiscountDto.category(), categoryDiscountDto.discount());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Product",
            description = "Delete a single product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid information our empty information")
    })
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String productId){
        return productService.removeProduct(productId);
    }

}
