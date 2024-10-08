package com.is4tech.invoicemanagement.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InvoiceDto {

    private Integer invoiceId;
    
    @JsonIgnore
    private Date creationDate;
    
    private Integer discount;

    @NotNull(message = "El subTotal es obligatorio")
    private Double subtotal;
    
    @NotNull(message = "El total completo es obligatorio")
    private Double total;
    
    @NotNull(message = "El metodo de pago es obligatorio")
    private Integer paymentMethodId;
    
    @NotNull(message = "El cliente es obligatorio")
    private Integer customer;

    private Integer statusInvoiceId;
    
    @NotNull(message = "El usuario del registro es obligatorio")
    private Long userId;
}
