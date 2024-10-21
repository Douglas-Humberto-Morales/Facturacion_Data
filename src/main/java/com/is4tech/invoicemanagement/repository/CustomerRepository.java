package com.is4tech.invoicemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.is4tech.invoicemanagement.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer>{
    List<Customer> findByNameContainingIgnoreCase(String name);
}
