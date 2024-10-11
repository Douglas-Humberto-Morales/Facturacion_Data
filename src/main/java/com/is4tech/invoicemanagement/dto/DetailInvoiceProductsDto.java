package com.is4tech.invoicemanagement.dto;

import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.DecimalMin;
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
public class DetailInvoiceProductsDto {
    
    @JsonIgnore
    private Integer detailInvoiceProductsId;
    @NotEmpty(message = "Name is required")
    @NotBlank(message = "Name not blank")
    private String name;
    @NotEmpty(message = "Price is required")
    @NotBlank(message = "Potal not blank")
    @DecimalMin(value = "0.01", message = "Total must be greater than 0")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double price;
}
