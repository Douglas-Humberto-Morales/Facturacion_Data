package com.is4tech.invoicemanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerDto {
    @JsonIgnore
    private Integer customerId;
    @NotEmpty(message = "Name is required")
    @NotBlank(message = "Name not blank")
    private String name;
    private String dpi;
    private String passport;
    private String nit;
    @NotEmpty(message = "Address is required")
    @NotBlank(message = "Address not blank")
    private String address;
    @JsonIgnore
    private Boolean status;
}
