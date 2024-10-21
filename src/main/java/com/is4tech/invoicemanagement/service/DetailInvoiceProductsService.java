package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.model.DetailInvoiceProducts;
import com.is4tech.invoicemanagement.model.Invoice;
import com.is4tech.invoicemanagement.repository.DetailInvoiceProductsRepository;
import com.is4tech.invoicemanagement.repository.InvoiceRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DetailInvoiceProductsService {
    private final DetailInvoiceProductsRepository detailInvoiceProductsRepository;

    private final InvoiceRepository invoiceRepository; 

    public MessagePage findByDetailInvoiceProduct(Pageable pageable) {
        Page<DetailInvoiceProducts> listaAlldetailInvoiceProducts = detailInvoiceProductsRepository.findAll(pageable);

        return MessagePage.builder()
            .note("Reports Retrieved Successfully")
            .object(listaAlldetailInvoiceProducts.getContent().stream().map(this::toDto).toList())
            .totalElements((int) listaAlldetailInvoiceProducts.getTotalElements())
            .totalPages(listaAlldetailInvoiceProducts.getTotalPages())
            .currentPage(listaAlldetailInvoiceProducts.getNumber())
            .pageSize(listaAlldetailInvoiceProducts.getSize())
            .build();
    }

    public DetailInvoiceProductsDto findByIdDetailInvoiceProducts(Integer id) {
        return detailInvoiceProductsRepository.findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new RuntimeException("Detail Invoice Products not found"));
    }

    public boolean existDetailInvoiceProducts(Integer id) {
        return detailInvoiceProductsRepository.existsById(id);
    }

    public DetailInvoiceProductsDto saveDetailInvoiceProducts(DetailInvoiceProductsDto detailInvoiceProductsDto, HttpServletRequest request) {
        try {
            DetailInvoiceProducts detailInvoiceProducts = toModel(detailInvoiceProductsDto);
            
            DetailInvoiceProducts saveDetailInvoiceProducts = detailInvoiceProductsRepository.save(detailInvoiceProducts);
            return toDto(saveDetailInvoiceProducts);
        } catch (Exception e) {
            throw e;
        }
    }

    private DetailInvoiceProducts toModel(DetailInvoiceProductsDto detailInvoiceProductsDto) {
        Invoice invoice = invoiceRepository.findById(detailInvoiceProductsDto.getInvoiceId())
            .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return DetailInvoiceProducts.builder()
            .detailInvoiceProductsId(detailInvoiceProductsDto.getDetailInvoiceProductsId())
            .name(detailInvoiceProductsDto.getName())
            .price(detailInvoiceProductsDto.getPrice())
            .amount(detailInvoiceProductsDto.getAmount())
            .invoice(invoice)
            .build();
    }

    private DetailInvoiceProductsDto toDto(DetailInvoiceProducts detailInvoiceProducts) {
        return DetailInvoiceProductsDto.builder()
            .detailInvoiceProductsId(detailInvoiceProducts.getDetailInvoiceProductsId())
            .name(detailInvoiceProducts.getName())
            .price(detailInvoiceProducts.getPrice())
            .amount(detailInvoiceProducts.getAmount())
            .invoiceId(detailInvoiceProducts.getInvoice().getInvoiceId())
            .build();
    }
}
