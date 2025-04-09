package com.base.spring.controller;


import com.base.spring.dto.ProductDto;
import com.base.spring.dto.CustomPageRequest;
import com.base.spring.dto.MessageResponse;
import com.base.spring.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@Tag(name = "Book API", description = "API untuk mengelola product.")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Add Product", description = "")
    @PostMapping("/add")
    public ResponseEntity<MessageResponse> addProduct(@RequestBody ProductDto request) {
        return productService.addProduct(request);
    }

    @Operation(summary = "Get All Product", description = "")
    @GetMapping("/all")
    public ResponseEntity<MessageResponse> getAllProduct(@ModelAttribute CustomPageRequest customPageRequest) {
        return productService.getAllProduct(customPageRequest.getPage("id,asc"));
    }

    @Operation(summary = "Get Product by id", description = "")
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @Operation(summary = "Update product", description = "")
    @PutMapping("/update/{id}")
    public ResponseEntity<MessageResponse> updateProduct(@PathVariable @Min(0) Long id, @RequestBody ProductDto request){
        return productService.updateProduct(id, request);
    }

    @Operation(summary = "Delete product", description = "")
    @PutMapping("/delete/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable @Min(0) Long id) {
        return productService.deleteBook(id);
    }

}
