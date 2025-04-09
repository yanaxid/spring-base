package com.base.spring.service;

import java.io.IOException;
import java.util.List;

import com.base.spring.dto.ProductDto;
import com.base.spring.dto.MessageResponse;

import static com.base.spring.enums.MessageKey.*;

import com.base.spring.exception.NotFoundException;
import com.base.spring.model.Product;
import com.base.spring.repository.ProductRepository;
import com.base.spring.util.ErrorDtoFactory;
import com.base.spring.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ResponseUtil responseUtil;
    private final ErrorDtoFactory error;


    @Transactional
    public ResponseEntity<MessageResponse> addProduct(ProductDto request) {

        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stok(request.getStok())
                .deleted(false)
                .build();

        productRepository.save(product);
        return responseUtil.successWithData(SUCCESS_SAVE_DATA, product);
    }


    public ResponseEntity<MessageResponse> getAllProduct(Pageable pageable) {

        Page<Product> products = productRepository.findAll(pageable);
        List<Product> productList = products.getContent();
        MessageResponse.Meta meta = MessageResponse.Meta.builder()
                .total(products.getTotalElements())
                .perPage(pageable.getPageSize())
                .currentPage(pageable.getPageNumber())
                .lastPage(products.getTotalPages())
                .build();

        return responseUtil.successWithDataAndMeta(SUCCESS_GET_DATA, productList, meta);
    }


    public ResponseEntity<MessageResponse> getProduct(Long id) {
        Product product = getValidProduct(id);
        return responseUtil.successWithData(SUCCESS_GET_DATA, product);
    }


    @Transactional
    public ResponseEntity<MessageResponse> updateProduct(Long id, ProductDto request) {

        Product product = getValidProduct(id);

        if (product.getName().equals(request.getName()) &&
                product.getPrice().equals(request.getPrice()) &&
                product.getStok().equals(request.getStok())) {
            return responseUtil.success(SUCCESS_NO_CHANGE);
        }

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStok(request.getStok());

        productRepository.save(product);

        return responseUtil.successWithData(SUCCESS_UPDATE_DATA, product);
    }


    @Transactional
    public ResponseEntity<MessageResponse> deleteBook(Long id) {

        Product product = getValidProduct(id);

        product.setDeleted(true);
        productRepository.save(product);

        return responseUtil.success(SUCCESS_DELETE_DATA, "Product id " + id);
    }

    private Product getValidProduct(Long id) {
        return productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new NotFoundException(
                        List.of(error.from("id", NOT_FOUND, "Product id " + id))
                ));
    }


}
