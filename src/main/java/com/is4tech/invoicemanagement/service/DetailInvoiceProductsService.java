package com.is4tech.invoicemanagement.service;

import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.repository.DetailInvoiceProductsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DetailInvoiceProductsService {
    private final DetailInvoiceProductsRepository detailInvoiceProductsRepository;
}
