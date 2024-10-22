package com.is4tech.invoicemanagement.service;

import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.dto.InvoiceDto;
import com.is4tech.invoicemanagement.dto.PaymentMethodDto;
import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.model.Invoice;
import com.is4tech.invoicemanagement.model.PaymentMethod;
import com.is4tech.invoicemanagement.model.StatusInvoice;
import com.is4tech.invoicemanagement.repository.InvoiceRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final PaymentMethodService paymentMethodService;
    private final StatusInvoiceService statusInvoiceService;
    private final CustomerService customerService;

    public InvoiceService(InvoiceRepository invoiceRepository, PaymentMethodService paymentMethodService,
                          StatusInvoiceService statusInvoiceService, CustomerService customerService) {
        this.invoiceRepository = invoiceRepository;
        this.paymentMethodService = paymentMethodService;
        this.statusInvoiceService = statusInvoiceService;
        this.customerService = customerService;
    }

    public List<InvoiceDto> findByAllInvoice(Pageable pageable){
        return invoiceRepository.findAll(pageable).stream()
                .map(this::toDto)
                .toList();
    }

    public InvoiceDto findByIdInvoice(Integer id){
        return invoiceRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
    }

    public boolean existInvoice(Integer id){
        return invoiceRepository.existsById(id);
    }

    public InvoiceDto saveInvoice(InvoiceDto invoiceDto, HttpServletRequest request){
        Invoice saveInvoice = toModel(invoiceDto, request);
        return toDto(invoiceRepository.save(saveInvoice));
    }

    public void changeStatusInvoice(InvoiceDto invoiceDto, Integer id){
        //Pendiente
    }

    private InvoiceDto toDto(Invoice invoice){
        return InvoiceDto.builder()
                .invoiceId(invoice.getInvoiceId())
                .creationDate(invoice.getCreationDate())
                .subtotal(invoice.getSubtotal())
                .total(invoice.getTotal())
                .paymentMethodId(invoice.getPaymentMethod().getPaymentMethodId())
                .customer(invoice.getCustomer().getCustomerId())
                .statusInvoiceId(invoice.getStatusInvoice().getStatusInvoiceId())
                .userId(invoice.getUserId())
                .build();
    }

    private Invoice toModel(InvoiceDto invoiceDto, HttpServletRequest request){
        PaymentMethod paymentMethod =
                toModelPaymentMethod(paymentMethodService.findByIdPaymentMethodDto(invoiceDto.getPaymentMethodId(), request));
        Customer customer =
                toModelCustomer(customerService.findByIdCustomer(invoiceDto.getCustomer(), request));
        StatusInvoice statusInvoice =
                toModelStatusInvoice(statusInvoiceService.findByIdStatusInvoice(invoiceDto.getStatusInvoiceId(), request));
        return Invoice.builder()
                .invoiceId(invoiceDto.getInvoiceId())
                .creationDate(invoiceDto.getCreationDate())
                .subtotal(invoiceDto.getSubtotal())
                .total(invoiceDto.getTotal())
                .paymentMethod(paymentMethod)
                .customer(customer)
                .statusInvoice(statusInvoice)
                .userId(invoiceDto.getUserId())
                .build();
    }

    private PaymentMethod toModelPaymentMethod(PaymentMethodDto paymentMethodDto){
        return PaymentMethod.builder()
                .paymentMethodId(paymentMethodDto.getPaymentMethodId())
                .name(paymentMethodDto.getName())
                .build();
    }

    private StatusInvoice toModelStatusInvoice(StatusInvoiceDto statusInvoiceDto){
        return StatusInvoice.builder()
                .statusInvoiceId(statusInvoiceDto.getStatudInvoiceId())
                .name(statusInvoiceDto.getName())
                .build();
    }

    private Customer toModelCustomer(CustomerDto customerDto){
        return Customer.builder()
                .customerId(customerDto.getCustomerId())
                .name(customerDto.getName())
                .dpi(customerDto.getDpi())
                .passport(customerDto.getPassport())
                .nit(customerDto.getNit())
                .address(customerDto.getAddress())
                .status(customerDto.getStatus())
                .build();
    }
}
