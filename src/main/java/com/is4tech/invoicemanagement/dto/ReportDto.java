package com.is4tech.invoicemanagement.dto;

import org.springframework.format.annotation.NumberFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportDto {

    private Integer noOrdered;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double amount;

    @NotNull(message = "Taxes is required")
    @DecimalMin(value = "0.00", message = "Taxes must be non-negative")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double taxes;

    @NotNull(message = "Total is required")
    @DecimalMin(value = "0.01", message = "Total must be greater than 0")
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private Double total;

    @NotNull(message = "Customer Id is required")
    @Min(value = 1, message = "Customer Id must be greater than 0")
    private Integer customerId;
}
