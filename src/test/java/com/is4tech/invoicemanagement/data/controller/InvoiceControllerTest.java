package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.InvoiceController;
import com.is4tech.invoicemanagement.dto.InvoiceDto;
import com.is4tech.invoicemanagement.exception.BadRequestException;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.InvoiceService;
import com.is4tech.invoicemanagement.utils.Message;
import jakarta.servlet.http.HttpServletRequest;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InvoiceControllerTest {

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private InvoiceController invoiceController;

    private InvoiceDto invoiceDto;
    private int id = 1;

    private static final String MESSAJE_DB_ERROR = "DB error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        invoiceDto = InvoiceDto.builder()
                .invoiceId(id)
                .subtotal(100.0)
                .total(120.0)
                .paymentMethodId(1)
                .customer(1)
                .userId(1)
                .build();
    }

    @Test
    void showAllInvoices() {
        Pageable pageable = PageRequest.of(0, 10);
        List<InvoiceDto> listInvoice = new ArrayList<>();
        listInvoice.add(invoiceDto);

        when(invoiceService.findByAllInvoice(pageable)).thenReturn(listInvoice);

        ResponseEntity<Message> response = invoiceController.showAllInvoices(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(invoiceService, times(1)).findByAllInvoice(pageable);
    }

    @Test
    void showAllInvoicesNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(invoiceService.findByAllInvoice(pageable)).thenReturn(new ArrayList<>());

        assertThrows(ResourceNorFoundException.class, () -> invoiceController.showAllInvoices(pageable));
    }

    @Test
    void showByIdInvoice() {
        when(invoiceService.findByIdInvoice(id)).thenReturn(invoiceDto);

        ResponseEntity<Message> response = invoiceController.showByIdInvoice(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(invoiceService, times(1)).findByIdInvoice(id);
    }

    @Test
    void showByIdInvoiceNotFound() {
        when(invoiceService.findByIdInvoice(id)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () -> invoiceController.showByIdInvoice(id));
    }

    @Test
    void changeStatusInvoice() throws Exception {
        when(invoiceService.existInvoice(id)).thenReturn(true);
        doNothing().when(invoiceService).changeStatusInvoice(any(InvoiceDto.class), eq(id));

        ResponseEntity<Message> response = invoiceController.changeStatusInvoice(invoiceDto, id);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Invoice change status successfully", response.getBody().getNote());
        verify(invoiceService, times(1)).changeStatusInvoice(any(InvoiceDto.class), eq(id));
    }

    @Test
    void changeStatusInvoiceNotFound() {
        when(invoiceService.existInvoice(id)).thenReturn(false);

        assertThrows(ResourceNorFoundException.class, () -> invoiceController.changeStatusInvoice(invoiceDto, id));
    }

    @Test
    void changeStatusInvoiceDataAccessException() {
        when(invoiceService.existInvoice(id)).thenReturn(true);
        doThrow(new DataAccessException(MESSAJE_DB_ERROR) {
        })
                .when(invoiceService).changeStatusInvoice(any(InvoiceDto.class), eq(id));

        BadRequestException thrown = assertThrows(BadRequestException.class,
                () -> invoiceController.changeStatusInvoice(invoiceDto, id));

        assertEquals("Error update record: DB error", thrown.getMessage());
    }
}
