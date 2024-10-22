package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.PaymentMethodController;
import com.is4tech.invoicemanagement.dto.PaymentMethodDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.PaymentMethodService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private PaymentMethodDto paymentMethodDto;
    private int id = 1;

    private static final String MESSAGE_UNEXPECTED = "Unexpected error";
    private static final String MESSAGE_DB_ERROR = "DB error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentMethodDto = PaymentMethodDto.builder()
                .paymentMethodId(id)
                .name("Credit Card")
                .build();
    }

    @Test
    void showAllInvoiceStatements() {
        Pageable pageable = PageRequest.of(0, 10);
        MessagePage messagePage = new MessagePage();

        when(paymentMethodService.findByAllPaymentMethod(pageable, request)).thenReturn(messagePage);

        ResponseEntity<Message> response = paymentMethodController.showAllInvoiceStatements(pageable, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(paymentMethodService, times(1)).findByAllPaymentMethod(pageable, request);
    }

//    @Test
//    void showByIdPaymentMethod() {
//        when(paymentMethodService.findByIdPaymentMethodDto(id, request)).thenReturn(paymentMethodDto);
//
//        ResponseEntity<Message> response = paymentMethodController.showByIdPaymentMethod(id, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("Record found", response.getBody().getNote());
//        verify(paymentMethodService, times(1)).findByIdPaymentMethodDto(id, request);
//    }

    @Test
    void showByIdPaymentMethodNotFound() {
        when(paymentMethodService.findByIdPaymentMethodDto(id, request)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () ->
                paymentMethodController.showByIdPaymentMethod(id, request)
        );
    }

    @Test
    void savePaymentMethod() throws Exception {
        when(paymentMethodService.savePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class)))
                .thenReturn(paymentMethodDto);

        ResponseEntity<Message> response = paymentMethodController.savePaymentMethod(paymentMethodDto, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved successfully", response.getBody().getNote());
        verify(paymentMethodService, times(1))
            .savePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));
    }

    @Test
    void savePaymentMethodDataAccessException() {
        when(paymentMethodService.savePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException(MESSAGE_DB_ERROR) {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                paymentMethodController.savePaymentMethod(paymentMethodDto, request)
        );

        assertEquals("Error save record: DB error", thrown.getMessage());
    }

    @Test
    void updatePaymentMethod() throws Exception {
        when(paymentMethodService.existPaymentMethod(id)).thenReturn(true);
        when(paymentMethodService.updatePaymentMethodDto(eq(paymentMethodDto), eq(id), any(HttpServletRequest.class)))
                .thenReturn(paymentMethodDto);

        ResponseEntity<Message> response = paymentMethodController.updatePaymentMethod(paymentMethodDto, id, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved successfully", response.getBody().getNote());
        verify(paymentMethodService, times(1))
            .updatePaymentMethodDto(eq(paymentMethodDto), eq(id), any(HttpServletRequest.class));
    }

    @Test
    void updatePaymentMethodNotFound() {
        when(paymentMethodService.existPaymentMethod(id)).thenReturn(false);

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                paymentMethodController.updatePaymentMethod(paymentMethodDto, id, request)
        );

        assertEquals("Unexpected error occurred: Payment Method was not found with: payment_method_id = '1'", 
            thrown.getMessage());
    }

    @Test
    void updatePaymentMethodDataAccessException() {
        when(paymentMethodService.existPaymentMethod(id)).thenReturn(true);
        when(paymentMethodService.updatePaymentMethodDto(eq(paymentMethodDto), eq(id), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException(MESSAGE_DB_ERROR) {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                paymentMethodController.updatePaymentMethod(paymentMethodDto, id, request)
        );

        assertEquals("Error updating record: DB error", thrown.getMessage());
    }

    @Test
    void deletePaymentMethod() throws Exception {
        when(paymentMethodService.findByIdPaymentMethodDto(id, request)).thenReturn(paymentMethodDto);
        doNothing().when(paymentMethodService).deletePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));

        ResponseEntity<Message> response = paymentMethodController.deletePaymentMethod(id, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(paymentMethodService, times(1)).deletePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));
    }

//    @Test
//    void deletePaymentMethodNotFound() {
//        when(paymentMethodService.findByIdPaymentMethodDto(id, request))
//                .thenThrow(new ResourceNorFoundException("Payment Method not found"));
//
//        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
//                paymentMethodController.deletePaymentMethod(id, request)
//        );
//
//        assertEquals("Payment Method not found: There are no Payment Method not found records in the system",
//            thrown.getMessage());
//    }

    @Test
    void deletePaymentMethodDataAccessException() {
        when(paymentMethodService.findByIdPaymentMethodDto(anyInt(), any(HttpServletRequest.class)))
            .thenReturn(paymentMethodDto);

        doThrow(new DataAccessException(MESSAGE_DB_ERROR) {}).when(paymentMethodService)
            .deletePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> 
            paymentMethodController.deletePaymentMethod(id, request)
        );

        assertEquals("Error deleting record: DB error", thrown.getMessage());

        verify(paymentMethodService, times(1))
            .deletePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));
    }

    @Test
    void deletePaymentMethodUnhandledException() {
        when(paymentMethodService.findByIdPaymentMethodDto(anyInt(), any(HttpServletRequest.class)))
            .thenReturn(paymentMethodDto);

        doThrow(new RuntimeException(MESSAGE_UNEXPECTED)).when(paymentMethodService)
            .deletePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> 
            paymentMethodController.deletePaymentMethod(id, request)
        );

        assertEquals("Unexpected error occurred: Unexpected error", thrown.getMessage());

        verify(paymentMethodService, times(1))
            .deletePaymentMethod(any(PaymentMethodDto.class), any(HttpServletRequest.class));
    }
}
