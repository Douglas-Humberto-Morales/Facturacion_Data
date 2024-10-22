package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.StatusInvoiceController;
import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.StatusInvoiceService;
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

class StatusInvoiceControllerTest {

    @Mock
    private StatusInvoiceService statusInvoiceService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private StatusInvoiceController statusInvoiceController;

    private StatusInvoiceDto statusInvoiceDto;
    private int id = 1;

    private static final String MESSAJE_UNEXPEDTED = "Unexpected error";
    private static final String MESSAJE_DB_ERROR = "DB error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statusInvoiceDto = StatusInvoiceDto.builder()
                .statudInvoiceId(id)
                .name("Activo")
                .build();
    }

    @Test
    void showAllInvoiceStatements() {
        Pageable pageable = PageRequest.of(0, 10);
        MessagePage messagePage = new MessagePage();

        when(statusInvoiceService.listAllStatusInvoice(pageable, request)).thenReturn(messagePage);

        ResponseEntity<Message> response = statusInvoiceController.showAllInvoiceStatements(pageable, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(statusInvoiceService, times(1)).listAllStatusInvoice(pageable, request);
    }

    @Test
    void showByIdStatusInvoice() {
        when(statusInvoiceService.findByIdStatusInvoice(id, request)).thenReturn(statusInvoiceDto);

        ResponseEntity<Message> response = statusInvoiceController.showByIdStatusInvoice(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Record found", response.getBody().getNote());
        verify(statusInvoiceService, times(1)).findByIdStatusInvoice(id, request);
    }

    @Test
    void showByIdStatusInvoiceNotFound() {
        when(statusInvoiceService.findByIdStatusInvoice(id, request)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () ->
                statusInvoiceController.showByIdStatusInvoice(id, request)
        );
    }

    @Test
    void saveStatusInvoice() throws Exception {
        when(statusInvoiceService.saveStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class)))
                .thenReturn(statusInvoiceDto);

        ResponseEntity<Message> response = statusInvoiceController.saveStatusInvoice(statusInvoiceDto, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved successfully", response.getBody().getNote());
        verify(statusInvoiceService, times(1))
            .saveStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));
    }

    @Test
    void saveStatusInvoiceDataAccessException() {
        when(statusInvoiceService.saveStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException(MESSAJE_DB_ERROR) {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                statusInvoiceController.saveStatusInvoice(statusInvoiceDto, request)
        );

        assertEquals("Error save record: DB error", thrown.getMessage());
    }

    @Test
    void updateStatusInvoice() throws Exception {
        when(statusInvoiceService.existStatusInvoice(id)).thenReturn(true);
        when(statusInvoiceService
            .updateStatusInvoice(eq(id), any(StatusInvoiceDto.class), any(HttpServletRequest.class)))
                .thenReturn(statusInvoiceDto);

        ResponseEntity<Message> response = statusInvoiceController.updateStatusInvoice(statusInvoiceDto, id, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved successfully", response.getBody().getNote());
        verify(statusInvoiceService, times(1))
            .updateStatusInvoice(eq(id), any(StatusInvoiceDto.class), any(HttpServletRequest.class));
    }

    @Test
    void updateStatusInvoiceNotFound() {
        when(statusInvoiceService.existStatusInvoice(id)).thenReturn(false);

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                statusInvoiceController.updateStatusInvoice(statusInvoiceDto, id, request)
        );

        assertEquals("Unexpected error occurred: Status Invoice was not found with: status_invoice_id = '1'", 
            thrown.getMessage());
    }

    @Test
    void updateStatusInvoiceDataAccessException() throws BadRequestException {
        when(statusInvoiceService.existStatusInvoice(id)).thenReturn(true);
        when(statusInvoiceService.updateStatusInvoice(eq(id), any(StatusInvoiceDto.class), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException(MESSAJE_DB_ERROR) {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                statusInvoiceController.updateStatusInvoice(statusInvoiceDto, id, request)
        );

        assertEquals("Error updating record: DB error", thrown.getMessage());
    }

    @Test
    void deleteStatusInvoice() throws Exception {
        when(statusInvoiceService.findByIdStatusInvoice(id, request)).thenReturn(statusInvoiceDto);
        doNothing().when(statusInvoiceService).deleteStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));

        ResponseEntity<Message> response = statusInvoiceController.deleteStatusInvoice(id, request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(statusInvoiceService, times(1)).deleteStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));
    }

    @Test
    void deleteStatusInvoiceNotFound() {
        when(statusInvoiceService.findByIdStatusInvoice(id, request))
                .thenThrow(new ResourceNorFoundException("Status Invoice not found"));

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                statusInvoiceController.deleteStatusInvoice(id, request)
        );

        assertEquals("Status Invoice not found: There are no Status Invoice not found records in the system", 
            thrown.getMessage());
    }


    @Test
    void deleteStatusInvoiceDataAccessException() throws BadRequestException {
        when(statusInvoiceService.findByIdStatusInvoice(anyInt(), any(HttpServletRequest.class)))
            .thenReturn(statusInvoiceDto);

        doThrow(new DataAccessException(MESSAJE_DB_ERROR) {}).when(statusInvoiceService)
            .deleteStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> 
            statusInvoiceController.deleteStatusInvoice(id, request)
        );

        assertEquals("Error deleting record: DB error", thrown.getMessage());

        verify(statusInvoiceService, times(1))
            .deleteStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));
    }

    @Test
    void deleteStatusInvoiceUnhandledException() throws BadRequestException {
        when(statusInvoiceService.findByIdStatusInvoice(anyInt(), any(HttpServletRequest.class)))
            .thenReturn(statusInvoiceDto);

        doThrow(new RuntimeException(MESSAJE_UNEXPEDTED)).when(statusInvoiceService)
            .deleteStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));

        BadRequestException thrown = assertThrows(BadRequestException.class, () -> 
            statusInvoiceController.deleteStatusInvoice(id, request)
        );

        assertEquals("Unexpected error occurred: Unexpected error", thrown.getMessage());

        verify(statusInvoiceService, times(1))
            .deleteStatusInvoice(any(StatusInvoiceDto.class), any(HttpServletRequest.class));
    }

    @Test
    void showAllStatusInvoiceNotPag() {
        List<StatusInvoiceDto> statusInvoiceList = new ArrayList<>();
        statusInvoiceList.add(statusInvoiceDto);

        when(statusInvoiceService.findByAllPaymentMethodNotPageable()).thenReturn(statusInvoiceList);

        ResponseEntity<Message> response = statusInvoiceController.showAllStatusInvoiceNotPag();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(statusInvoiceService, times(1)).findByAllPaymentMethodNotPageable();
    }
}
