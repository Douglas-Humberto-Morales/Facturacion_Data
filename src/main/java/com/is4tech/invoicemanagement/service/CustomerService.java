package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.exception.BadRequestException;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.repository.CustomerRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    
    private static final String NAME_ENTITY = "Customer";
    private static final String ID_ENTITY = "customer_id";

    public MessagePage findByAllCustomer(Pageable pageable, HttpServletRequest request){
        Page<Customer> listAllCustomer = customerRepository.findAll(pageable);
        /*
        if(listAllCustomer.isEmpty()){
            int statusCode = HttpStatus.NOT_FOUND.value();
            auditService.logAudit(null, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, pageable.toString());
        }

        int statusCode = HttpStatus.OK.value();
        auditService.logAudit(listAllCustomer.getContent(), this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        */

        return MessagePage.builder()
            .note("Reports Retrieved Successfully")
            .object(listAllCustomer.getContent().stream().map(this::toDto).toList())
            .totalElements((int) listAllCustomer.getTotalElements())
            .totalPages(listAllCustomer.getTotalPages())
            .currentPage(listAllCustomer.getNumber())
            .pageSize(listAllCustomer.getSize())
            .build();
    }

    public CustomerDto findByIdCustomer(Integer id, HttpServletRequest request){
        CustomerDto customerDto = customerRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Cutomer not found"));
        //int statusCode = HttpStatus.OK.value();
        //auditService.logAudit(customerDto, this.getClass().getMethods()[0], null, statusCode, "Customer", request);
        return customerDto;
    }

    public boolean existCustomer(Integer id){
        return customerRepository.existsById(id);
    }

    public CustomerDto saveCustomer(CustomerDto customerDto, HttpServletRequest request){
        try {
            Customer customer = toModel(customerDto);
            Customer saveCustomer = customerRepository.save(customer);
            //int statusCode = HttpStatus.CREATED.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            return toDto(saveCustomer);
        } catch (Exception e) {
            //int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], e, statusCode, NAME_ENTITY, request);
            throw e;
        }
    }

    public void deleteCustomer(CustomerDto customerDto, HttpServletRequest request){
        try {
            Customer deleteCustomer = customerRepository.findById(customerDto.getCustomerId())
                    .orElseThrow(() -> new ResourceNorFoundException("Customer not found"));
            
            customerRepository.delete(deleteCustomer);

            //int statusCode = HttpStatus.NO_CONTENT.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], null, statusCode, "Customer", request);

        } catch (ResourceNorFoundException e) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], null, statusCode, "Customer", request);
            throw e;
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], null, statusCode, "Customer", request);
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    public CustomerDto updateCustomer(CustomerDto customerDto, Integer id, HttpServletRequest request){
        if (customerDto.getCustomerId() == null) {
            throw new BadRequestException("Customer ID cannot be null");
        }
        try {
            Customer updateCustomer = customerRepository.findById(id)
                    .orElseThrow(() -> new ResourceNorFoundException("Customer not found"));

            if (customerDto.getName() != null) {
                updateCustomer.setName(customerDto.getName());
            }
            if (customerDto.getDpi() != null) {
                updateCustomer.setDpi(customerDto.getDpi());
            }
            if (customerDto.getPassport() != null) {
                updateCustomer.setPassport(customerDto.getPassport());
            }
            if (customerDto.getNit() != null) {
                updateCustomer.setNit(customerDto.getNit());
            }
            if (customerDto.getAddress() != null) {
                updateCustomer.setAddress(customerDto.getAddress());
            }

            Customer customer = customerRepository.save(updateCustomer);

            int statusCode = HttpStatus.OK.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], null, statusCode, "Customer", request);

            return toDto(customer);
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(customerDto, this.getClass().getMethods()[0], e, statusCode, "Customer", request);
            throw new BadRequestException("Error updating Customer: " + e.getMessage());
        }
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
