package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.CustomerController;
import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.CustomerService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CustomerController customerController;

    private CustomerDto customerDto;
    private int id = 1;

    private static final String MESSAJE_UNEXPEDTED = "Unexpected error";
    private static final String MESSAJE_DB_ERROR = "DB error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerDto = CustomerDto.builder()
                .customerId(id)
                .name("Juan Perez")
                .address("123 Main St")
                .build();
    }

    @Test
    void showAllCustomer() {
        Pageable pageable = PageRequest.of(0, 10);
        MessagePage messagePage = new MessagePage();

        when(customerService.findByAllCustomer(pageable, request)).thenReturn(messagePage);

        ResponseEntity<Message> response = customerController.showAllCustomer(pageable, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(customerService, times(1)).findByAllCustomer(pageable, request);
    }

    @Test
    void showByIdCustomer() {
        when(customerService.findByIdCustomer(id, request)).thenReturn(customerDto);

        ResponseEntity<Message> response = customerController.showByIdCustomer(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Record found", response.getBody().getNote());
        verify(customerService, times(1)).findByIdCustomer(id, request);
    }

    @Test
    void showByIdCustomerNotFound() {
        when(customerService.findByIdCustomer(id, request)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () -> customerController.showByIdCustomer(id, request));
    }

    @Test
    void saveCustomer() throws Exception {
        when(customerService.saveCustomer(any(CustomerDto.class), any(HttpServletRequest.class)))
                .thenReturn(customerDto);

        ResponseEntity<Message> response = customerController.saveCustomer(customerDto, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved successfully", response.getBody().getNote());
        verify(customerService, times(1))
                .saveCustomer(any(CustomerDto.class), any(HttpServletRequest.class));
    }

    @Test
    void saveCustomerDataAccessException() {
        when(customerService.saveCustomer(any(CustomerDto.class), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException(MESSAJE_DB_ERROR) {
                });

        BadRequestException thrown = assertThrows(BadRequestException.class,
                () -> customerController.saveCustomer(customerDto, request));

        assertEquals("Error save record: DB error", thrown.getMessage());
    }

    @Test
    void updateCustomer() throws Exception {
        when(customerService.existCustomer(id)).thenReturn(true);
        when(customerService.updateCustomer(any(CustomerDto.class), eq(id), any(HttpServletRequest.class)))
                .thenReturn(customerDto);

        ResponseEntity<Message> response = customerController.updateCustomer(customerDto, id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update successfully", response.getBody().getNote());
        verify(customerService, times(1))
                .updateCustomer(any(CustomerDto.class), eq(id), any(HttpServletRequest.class));
    }

    @Test
    void updateCustomerNotFound() {
        when(customerService.existCustomer(id)).thenReturn(false);

        assertThrows(BadRequestException.class,
                () -> customerController.updateCustomer(customerDto, id, request));
    }

    @Test
    void updateCustomerDataAccessException() {
        when(customerService.existCustomer(id)).thenReturn(true);
        when(customerService.updateCustomer(any(CustomerDto.class), eq(id), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException(MESSAJE_DB_ERROR) {
                });

        BadRequestException thrown = assertThrows(BadRequestException.class,
                () -> customerController.updateCustomer(customerDto, id, request));

        assertEquals("Error updating record: DB error", thrown.getMessage());
    }

    @Test
    void deleteCustomer() throws Exception {
        when(customerService.findByIdCustomer(id, request)).thenReturn(customerDto);
        doNothing().when(customerService).deleteCustomer(any(CustomerDto.class), any(HttpServletRequest.class));

        ResponseEntity<Message> response = customerController.deleteCustomer(id, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(customerService, times(1))
                .deleteCustomer(any(CustomerDto.class), any(HttpServletRequest.class));
    }

    @Test
    void deleteCustomerDataAccessException() {
        when(customerService.findByIdCustomer(anyInt(), any(HttpServletRequest.class)))
                .thenReturn(customerDto);

        doThrow(new DataAccessException(MESSAJE_DB_ERROR) {
        }).when(customerService)
                .deleteCustomer(any(CustomerDto.class), any(HttpServletRequest.class));

        BadRequestException thrown = assertThrows(BadRequestException.class,
                () -> customerController.deleteCustomer(id, request));

        assertEquals("Error delete record: DB error", thrown.getMessage());
        verify(customerService, times(1))
                .deleteCustomer(any(CustomerDto.class), any(HttpServletRequest.class));
    }

    @Test
    void deleteCustomerUnhandledException() {
        when(customerService.findByIdCustomer(anyInt(), any(HttpServletRequest.class)))
                .thenReturn(customerDto);

        doThrow(new RuntimeException(MESSAJE_UNEXPEDTED)).when(customerService)
                .deleteCustomer(any(CustomerDto.class), any(HttpServletRequest.class));

        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> customerController.deleteCustomer(id, request));

        assertEquals("Unexpected error", thrown.getMessage());
        verify(customerService, times(1))
                .deleteCustomer(any(CustomerDto.class), any(HttpServletRequest.class));
    }
}
