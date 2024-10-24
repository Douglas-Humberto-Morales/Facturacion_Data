package com.is4tech.invoicemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "detail_invoice_products")
public class DetailInvoiceProducts {

    @Id
    @Column(name = "detail_invoice_products_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer detailInvoiceProductsId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @Column(name = "amount")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "invoice_id", referencedColumnName = "invoice_id")
    private Invoice invoice;
}
