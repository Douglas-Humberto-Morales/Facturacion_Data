package com.is4tech.invoicemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CustomerDto {
    private Integer customerId;

    @NotBlank(message = "El nombre completo es obligatorio")
    private String name;
    private String dpi;
    private String passport;
    private String nit;
    @NotBlank(message = "La direccion es obligatoria")
    private String address;
    private Boolean status;
}
