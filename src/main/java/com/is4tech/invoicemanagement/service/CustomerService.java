package com.is4tech.invoicemanagement.service;

import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
}
