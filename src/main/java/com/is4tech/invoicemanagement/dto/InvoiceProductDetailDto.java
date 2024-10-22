package com.is4tech.invoicemanagement.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductDetailDto {
    private Integer detailInvoiceProductsId;
    private String name;
    private Double price;
    private Integer amount;
    private Double totalPrice;
    private Date creationDate;
    private Double subtotal; 
    private Double total;
}
