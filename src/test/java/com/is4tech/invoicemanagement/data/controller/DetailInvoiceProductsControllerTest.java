package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.DetailInvoiceProductsController;
import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.DetailInvoiceProductsService;
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

import java.util.ArrayList;
import java.util.List;

class DetailInvoiceProductsControllerTest {

    @Mock
    private DetailInvoiceProductsService detailInvoiceProductsService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private DetailInvoiceProductsController detailInvoiceProductsController;

    private DetailInvoiceProductsDto detailInvoiceProductsDto;
    private int id = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        detailInvoiceProductsDto = DetailInvoiceProductsDto.builder()
                .detailInvoiceProductsId(id)
                .name("Product A")
                .price(10.0)
                .amount(1)
                .invoiceId(1)
                .build();
    }

    @Test
    void showAllDetailInvoiceProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        MessagePage messagePage = new MessagePage();

        when(detailInvoiceProductsService.findByDetailInvoiceProduct(pageable)).thenReturn(messagePage);

        ResponseEntity<Message> response = detailInvoiceProductsController.showAllDetailInvoiceProducts(pageable, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(detailInvoiceProductsService, times(1)).findByDetailInvoiceProduct(pageable);
    }

    @Test
    void showByIdDetailInvoiceProducts() {
        when(detailInvoiceProductsService.findByIdDetailInvoiceProducts(id)).thenReturn(detailInvoiceProductsDto);

        ResponseEntity<Message> response = detailInvoiceProductsController.showByIdDetailInvoiceProducts(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records found", response.getBody().getNote());
        verify(detailInvoiceProductsService, times(1)).findByIdDetailInvoiceProducts(id);
    }

    @Test
    void showByIdDetailInvoiceProductsNotFound() {
        when(detailInvoiceProductsService.findByIdDetailInvoiceProducts(id)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () ->
                detailInvoiceProductsController.showByIdDetailInvoiceProducts(id, request)
        );
    }

    @Test
    void saveDetailInvoiceProducts() throws Exception {
        List<DetailInvoiceProductsDto> detailList = new ArrayList<>();
        detailList.add(detailInvoiceProductsDto);

        when(detailInvoiceProductsService.saveDetailInvoiceProducts(any(DetailInvoiceProductsDto.class), any(HttpServletRequest.class)))
                .thenReturn(detailInvoiceProductsDto);

        ResponseEntity<Message> response = detailInvoiceProductsController.saveDetailInvoiceProducts(detailList, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("All records saved successfully", response.getBody().getNote());
        verify(detailInvoiceProductsService, times(1))
                .saveDetailInvoiceProducts(any(DetailInvoiceProductsDto.class), any(HttpServletRequest.class));
    }

    @Test
    void saveDetailInvoiceProductsDataAccessException() {
        List<DetailInvoiceProductsDto> detailList = new ArrayList<>();
        detailList.add(detailInvoiceProductsDto);

        when(detailInvoiceProductsService.saveDetailInvoiceProducts(any(DetailInvoiceProductsDto.class), any(HttpServletRequest.class)))
                .thenThrow(new DataAccessException("DB error") {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                detailInvoiceProductsController.saveDetailInvoiceProducts(detailList, request)
        );

        assertEquals("Error saving records: DB error", thrown.getMessage());
    }
}
