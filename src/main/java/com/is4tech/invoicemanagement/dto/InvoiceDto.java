package com.is4tech.invoicemanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class InvoiceDto {
    private Integer invoiceId;

    @JsonIgnore
    private Date creationDate;

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
    private Integer userId;
}
