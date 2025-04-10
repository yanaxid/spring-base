package com.base.spring.controller;

import com.base.spring.dto.ProductDto;
import com.base.spring.dto.CustomPageRequest;
import com.base.spring.dto.MessageResponse;
import com.base.spring.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Product API", description = "API for managing product data.")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Add Product", description = "Create a new product.")
    @PostMapping
    public ResponseEntity<MessageResponse> addProduct(@Valid @RequestBody ProductDto request) {
        return productService.addProduct(request);
    }

    @Operation(summary = "Get All Products", description = "Retrieve all products with pagination.")
    @GetMapping
    public ResponseEntity<MessageResponse> getAllProducts(
            @RequestParam(required = false) String name,
            @Valid @ModelAttribute CustomPageRequest customPageRequest) {
        return productService.getAllProducts(name, customPageRequest.getPage("id,asc"));
    }

    @Operation(summary = "Get Product by ID", description = "Retrieve a product by its ID.")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getProduct(@PathVariable @Positive Long id) {
        return productService.getProduct(id);
    }

    @Operation(summary = "Update Product", description = "Update an existing product by ID.")
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable @Positive Long id, @Valid @RequestBody ProductDto request){
        return productService.updateProduct(id, request);
    }

    @Operation(summary = "Delete Product", description = "Delete a product by ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable @Positive Long id) {
        return productService.deleteProduct(id);
    }
}
