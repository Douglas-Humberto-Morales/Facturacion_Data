package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.repository.CustomerRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<CustomerDto> findByAllCustomer(Pageable pageable){
        return customerRepository.findAll(pageable).stream()
            .map(this::toDto)
            .toList();
    }

    public CustomerDto findByIdCustomer(Integer id){
        return customerRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Cutomer not found"));
    }

    public boolean existCustomer(Integer id){
        return customerRepository.existsById(id);
    }

    public CustomerDto saveCustomer(CustomerDto customerDto){
        Customer saveCustomer = toModel(customerDto);
        return toDto(customerRepository.save(saveCustomer));
    }

    public void deleteCustomer(Integer id){
        Customer deleteCustomer = customerRepository.findById(id).orElse(null);
        assert deleteCustomer != null;
        customerRepository.delete(deleteCustomer);
    }

    public CustomerDto updateCustomer(CustomerDto customerDto, Integer id){
        Customer updateCustomer = customerRepository.findById(id).orElse(null);
        assert updateCustomer != null;
        customerDto.setCustomerId(id);
        updateCustomer = toModel(customerDto);
        return toDto(customerRepository.save(updateCustomer));
    }

    private Customer toModel(CustomerDto customerDto){
        return Customer.builder()
            .customerId(customerDto.getCustomerId())
            .name(customerDto.getName())
            .dpi(customerDto.getDpi())
            .passport(customerDto.getPassport())
            .nit(customerDto.getNit())
            .address(customerDto.getAddress())
            .status(customerDto.getStatus())
            .build();
    }

    private CustomerDto toDto(Customer customer){
        return CustomerDto.builder()
            .customerId(customer.getCustomerId())
            .name(customer.getName())
            .dpi(customer.getDpi())
            .passport(customer.getPassport())
            .nit(customer.getNit())
            .address(customer.getAddress())
            .status(customer.getStatus())
            .build();
    }
}
