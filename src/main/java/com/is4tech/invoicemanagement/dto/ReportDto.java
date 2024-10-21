package com.is4tech.invoicemanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportDto {

    private Integer noOrdered;

    private String nameClient;

    private Double taxes;

    private Double total;
}
