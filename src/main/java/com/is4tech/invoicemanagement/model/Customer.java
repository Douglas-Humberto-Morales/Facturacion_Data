package com.is4tech.invoicemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customerId;

    @Column(name = "name")
    @NotEmpty(message =  "The name is required")
    private String name;

    @Column(name = "dpi")
    private String dpi;
    
    @Column(name = "passport")
    private String passport;
    
    @Column(name = "nit")
    private String nit;
    
    @Column(name = "address")
    @NotEmpty(message =  "The address is required")
    private String address;
    
    @Column(name = "status")
    private Boolean status;
}
