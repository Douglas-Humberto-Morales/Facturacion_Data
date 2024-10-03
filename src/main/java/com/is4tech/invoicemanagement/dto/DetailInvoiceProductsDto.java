package com.is4tech.invoicemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DetailInvoiceProductsDto {
    
    private Integer detailInvoiceProductsId;
    @NotBlank(message = "El nombre completo es obligatorio")
    private String name;
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private Double price;
}
