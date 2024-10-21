package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.ReportControlle;
import com.is4tech.invoicemanagement.dto.ReportDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.ReportService;
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
import static org.mockito.Mockito.*;

class ReportControllerTest {

    @Mock
    private ReportService reportService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ReportControlle reportControlle;

    private ReportDto reportDto;
    private int id = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportDto = ReportDto.builder()
                .noOrdered(id)
                .amount(100.00)
                .taxes(10.00)
                .total(110.00)
                .customerId(1)
                .build();
    }

    @Test
    void showAllReports() {
        Pageable pageable = PageRequest.of(0, 10);
        MessagePage messagePage = new MessagePage();

        when(reportService.listAllReport(pageable, request)).thenReturn(messagePage);

        ResponseEntity<Message> response = reportControlle.showAllReports(pageable, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(reportService, times(1)).listAllReport(pageable, request);
    }

    @Test
    void showByIdReports() {
        when(reportService.findByIdReport(id, request)).thenReturn(reportDto);

        ResponseEntity<Message> response = reportControlle.showByIdReports(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Record found: no_ordered", response.getBody().getNote());
        verify(reportService, times(1)).findByIdReport(id, request);
    }

    @Test
    void showByIdReportsNotFound() {
        when(reportService.findByIdReport(id, request)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () -> reportControlle.showByIdReports(id, request));
    }

    @Test
    void saveReport() throws Exception {
        when(reportService.saveReport(any(ReportDto.class), any(HttpServletRequest.class)))
                .thenReturn(reportDto);

        ResponseEntity<Message> response = reportControlle.saveReport(reportDto, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Record create", response.getBody().getNote());
        verify(reportService, times(1))
                .saveReport(any(ReportDto.class), any(HttpServletRequest.class));
    }

    @Test
    void saveReportDataAccessException() {
        when(reportService.saveReport(any(ReportDto.class), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException("DB error") {
                });

        BadRequestException thrown = assertThrows(BadRequestException.class,
                () -> reportControlle.saveReport(reportDto, request));

        assertEquals("Error saving record: ReportDB error", thrown.getMessage());
    }

    @Test
    void saveReportResourceNotFoundException() {
        when(reportService.saveReport(any(ReportDto.class), any(HttpServletRequest.class)))
                .thenThrow(new ResourceNorFoundException("Resource not found"));

        ResourceNorFoundException thrown = assertThrows(ResourceNorFoundException.class,
                () -> reportControlle.saveReport(reportDto, request));

        assertEquals("There are no Resource not found records in the system", thrown.getMessage());
    }

    @Test
    void saveReportUnexpectedException() {
        when(reportService.saveReport(any(ReportDto.class), any(HttpServletRequest.class)))
                .thenThrow(new RuntimeException("Unexpected error"));

        BadRequestException thrown = assertThrows(BadRequestException.class,
                () -> reportControlle.saveReport(reportDto, request));

        assertEquals("Unexpected error occurred: Unexpected error", thrown.getMessage());
    }

}
