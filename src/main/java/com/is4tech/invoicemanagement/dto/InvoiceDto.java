package com.is4tech.invoicemanagement.dto;

import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.model.PaymentMethod;
import com.is4tech.invoicemanagement.model.StatusInvoice;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceDto {
    private Integer invoiceId;

    private LocalDateTime creationDate;

    private Double discount;

    private Double subtotal;

    private Double total;

    private String namePaymentMethod;

    private String name;

    private String nameStatus;

    private Integer userId;
    private String fullName;
}
