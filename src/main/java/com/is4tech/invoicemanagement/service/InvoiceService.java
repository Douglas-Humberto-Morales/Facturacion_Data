package com.is4tech.invoicemanagement.service;

import com.is4tech.invoicemanagement.dto.InvoiceDto;
import com.is4tech.invoicemanagement.model.Invoice;
import com.is4tech.invoicemanagement.repository.InvoiceRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public MessagePage findByAllInvoices(Pageable pageable) {
        Page<Invoice> listAllInvoices = invoiceRepository.findAll(pageable);

        return MessagePage.builder()
                .note("Invoices Retrieved Successfully")
                .object(listAllInvoices.getContent().stream().map(this::toDto).toList())
                .totalElements((int)listAllInvoices.getTotalElements())
                .totalPages(listAllInvoices.getTotalPages())
                .currentPage(listAllInvoices.getNumber())
                .pageSize(listAllInvoices.getSize())
                .build();
    }

    private InvoiceDto toDto(Invoice invoice) {
        return InvoiceDto.builder()
                .invoiceId(invoice.getInvoiceId())
                .creationDate(invoice.getCreationDate())
                .discount(invoice.getDiscount())
                .subtotal(invoice.getSubtotal())
                .total(invoice.getTotal())
                .namePaymentMethod(invoice.getPaymentMethod().getName())
                .name(invoice.getCustomer().getName())
                .nameStatus(invoice.getStatusInvoice().getName())
                .userId(invoice.getUserId())
                .build();
    }
}
