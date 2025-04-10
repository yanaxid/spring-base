package com.base.spring.spesification;


import java.util.ArrayList;
import java.util.List;

import com.base.spring.model.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;



public class ProductSpecification {


    private ProductSpecification(){}

    public static Specification<Product> filterBy(String name) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (name != null && !name.isBlank()) {
                String nameLike = "%" + name.toLowerCase() + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), nameLike));
            }

            predicates.add(cb.isFalse(root.get("deleted")));

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}



