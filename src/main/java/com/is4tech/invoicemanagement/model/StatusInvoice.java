package com.is4tech.invoicemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@Table(name = "status_invoice")
public class StatusInvoice {
    
    @Id
    @Column(name = "status_invoice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statusInvoiceId;

    @Column(name = "name")
    @NotEmpty(message =  "The name is required")
    private String name;
}
