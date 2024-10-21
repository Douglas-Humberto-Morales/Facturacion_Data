package com.is4tech.invoicemanagement.controller;

import java.util.List;

import com.is4tech.invoicemanagement.model.Customer;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.dto.NameSearchDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.CustomerService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;

@RestController
@RequestMapping("/invoice-management/v0.1/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private static final String NAME_ENTITY = "Customer";
    private static final String ID_ENTITY = "customer_id";

    @GetMapping("/show-all")
    public ResponseEntity<Message> showAllCustomer(@PageableDefault(size = 10) Pageable pageable,
            HttpServletRequest request) {
        MessagePage listCustomer = customerService.findByAllCustomer(pageable, request);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listCustomer)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/show-all-customers")
    public ResponseEntity<Message> showAllCustomer() {
        Message listCustomer = customerService.findAllCustomers();

        return new ResponseEntity<>(
                Message.builder()
                        .note("Records found")
                        .object(listCustomer.getObject())
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/show-by-id/{id}")
    public ResponseEntity<Message> showByIdCustomer(@PathVariable Integer id, HttpServletRequest request) {
        CustomerDto customerDto = customerService.findByIdCustomer(id, request);
        if (customerDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Record found")
                .object(customerDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/createa")
    public ResponseEntity<Message> saveCustomer(@RequestBody @Valid CustomerDto customerDto,
            HttpServletRequest request) throws BadRequestException {
        try {
            customerDto.setStatus(true);
            CustomerDto saveCustomer = customerService.saveCustomer(customerDto, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(saveCustomer)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Message> updateCustomer(@RequestBody @Valid CustomerDto customerDto,
            @PathVariable Integer id, HttpServletRequest request) throws BadRequestException {
        try {
            if (!customerService.existCustomer(id)) {
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
            }
            customerDto.setCustomerId(id);
            CustomerDto customerUpdate = customerService.updateCustomer(customerDto, id, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Update successfully")
                    .object(customerUpdate)
                    .build(), HttpStatus.OK);

        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error updating record: " + exDt.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Message> deleteCustomer(@PathVariable Integer id,
            HttpServletRequest request) throws BadRequestException {
        try {
            CustomerDto customerDto = customerService.findByIdCustomer(id, request);
            customerService.deleteCustomer(customerDto, request);
            return new ResponseEntity<>(Message.builder()
                    .object(null)
                    .build(),
                    HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error delete record: " + exDt.getMessage());
        }
    }

    @PostMapping("/search-by-name")
    public ResponseEntity<MessagePage> searchCustomerByName(
            @RequestBody CustomerDto customerDto,
            HttpServletRequest request, Pageable pageable) {

        Page<CustomerDto> customerPage = customerService.findByName(customerDto.getName(), pageable);

        return new ResponseEntity<>(MessagePage.builder()
                .note("Records found")
                .object(customerPage.getContent())
                .totalElements((int) customerPage.getTotalElements())
                .totalPages(customerPage.getTotalPages())
                .currentPage(customerPage.getNumber())
                .pageSize(customerPage.getSize())
                .build(),
                HttpStatus.OK);
    }

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<Customer> toggleCustomerStatus(@PathVariable Integer id, HttpServletRequest request) {
        try {
            Customer updatedCustomer = customerService.toggleCustomerStatus(id);

            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (ResourceNorFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new com.is4tech.invoicemanagement.exception.BadRequestException("Error" + e.getMessage());
        }
    }
}