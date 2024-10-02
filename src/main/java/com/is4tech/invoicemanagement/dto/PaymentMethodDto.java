package com.is4tech.invoicemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentMethodDto {
    
    private Integer paymentMethodId;
    @NotBlank(message = "El nombre completo es obligatorio")
    private String name;
}
