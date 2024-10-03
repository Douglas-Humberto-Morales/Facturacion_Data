package com.is4tech.invoicemanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ProductDto {
    private Integer productsId;
    private String code;
    private String name;
    private Integer delivery_time;
    private String description;
    private Double price;
    private Boolean status;
    private String companyOrBrandName;
    private LocalDate expirationDate;
    private LocalDate entryDate;
    private Integer stock;
}
