package com.is4tech.invoicemanagement.repository;

import com.is4tech.invoicemanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
