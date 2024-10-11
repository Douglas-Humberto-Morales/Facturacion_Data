package com.is4tech.invoicemanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentMethodDto {
    
    @JsonIgnore
    private Integer paymentMethodId;
    @NotEmpty(message = "Name is required")
    @NotBlank(message = "Name not blank")
    private String name;
}
