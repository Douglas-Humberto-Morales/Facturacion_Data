package com.is4tech.invoicemanagement.controller;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
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

import jakarta.validation.Valid;

import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.CustomerService;
import com.is4tech.invoicemanagement.utils.Message;

@RestController
@RequestMapping("/invoice-management/v0.1/")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    private static final String NAME_ENTITY = "Customer";
    private static final String ID_ENTITY = "customer_id";

    @GetMapping("/customers")
    public ResponseEntity<Message> showAllCustomer (@PageableDefault(size = 10) Pageable pageable) {
        List<CustomerDto> listCustomer = customerService.findByAllCustomer(pageable);
        if (listCustomer.isEmpty())
            throw new ResourceNorFoundException(NAME_ENTITY);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listCustomer)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Message> showByIdCustomer (@PathVariable Integer id) {
        CustomerDto customerDto = customerService.findByIdCustomer(id);
        if (customerDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(customerDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/customer")
    public ResponseEntity<Message> saveCustomer (@RequestBody @Valid CustomerDto customerDto)
            throws BadRequestException {
        try {
            customerDto.setStatus(true);
            CustomerDto saveCustomer = customerService.saveCustomer(customerDto);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(saveCustomer)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/customer/{id}")
    public ResponseEntity<Message> updateCustomer(@RequestBody @Valid CustomerDto customerDto,
            @PathVariable Integer id) throws BadRequestException {
        try {
            if (customerService.existCustomer(id)) {
                CustomerDto updateCustomer = customerService.updateCustomer(customerDto, id);
                return new ResponseEntity<>(Message.builder()
                        .note("Saved successfully")
                        .object(updateCustomer)
                        .build(),
                        HttpStatus.CREATED);
            } else
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error update record: " + exDt.getMessage());
        }
    }

    @DeleteMapping("/customer/{id}")
    public ResponseEntity<Message> deleteCustomer (@PathVariable Integer id) throws BadRequestException {
        try {
            customerService.deleteCustomer(id);
            return new ResponseEntity<>(Message.builder()
                    .object(null)
                    .build(),
                    HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error delete record: " + exDt.getMessage());
        }
    }
}