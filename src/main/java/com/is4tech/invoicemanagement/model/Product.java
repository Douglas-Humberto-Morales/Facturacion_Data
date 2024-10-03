package com.is4tech.invoicemanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "products_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productID;

    @Column(name = "code", length = 25)
    private String code;

    @Column(name = "name", length = 75)
    private String name;

    @Column(name = "delivery_time")
    private Integer deliveryTime;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "company_or_brand_name", length = 75)
    private String companyOrBrandName;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "stock")
    private Integer stock;
}
