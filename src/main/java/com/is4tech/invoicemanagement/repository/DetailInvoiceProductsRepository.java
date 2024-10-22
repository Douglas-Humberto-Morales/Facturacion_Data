package com.is4tech.invoicemanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.is4tech.invoicemanagement.model.DetailInvoiceProducts;

@Repository
public interface DetailInvoiceProductsRepository extends JpaRepository<DetailInvoiceProducts,Integer>{
    List<DetailInvoiceProducts> findByInvoice_InvoiceId(Integer invoiceId);
}
