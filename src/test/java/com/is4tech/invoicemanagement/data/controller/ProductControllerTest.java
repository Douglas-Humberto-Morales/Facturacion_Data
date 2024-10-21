package com.is4tech.invoicemanagement.data.controller;

import com.is4tech.invoicemanagement.controller.ProductController;
import com.is4tech.invoicemanagement.dto.ProductDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.ProductService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;
import com.is4tech.invoicemanagement.exception.BadRequestException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;
    private int id = 1;

    private static final String MESSAGE_DB_ERROR = "DB error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productDto = ProductDto.builder()
                .productsId(id)
                .name("Sample Product")
                .build();
    }

    @Test
    void showAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDto> productPage = new PageImpl<>(Collections.singletonList(productDto));

        when(productService.findByAllProducts(pageable)).thenReturn(productPage);

        ResponseEntity<MessagePage> response = productController.showAllProducts(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Records Found", response.getBody().getNote());
        verify(productService, times(1)).findByAllProducts(pageable);
    }

    @Test
    void showAllProductsNotFound() {
        Pageable pageable = PageRequest.of(0, 10);
        when(productService.findByAllProducts(pageable)).thenReturn(Page.empty());

        assertThrows(ResourceNorFoundException.class, () ->
                productController.showAllProducts(pageable)
        );
    }

    @Test
    void showProductById() {
        when(productService.findByIdProduct(id)).thenReturn(productDto);

        ResponseEntity<Message> response = productController.showProductById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Record Found", response.getBody().getNote());
        verify(productService, times(1)).findByIdProduct(id);
    }

    @Test
    void showProductByIdNotFound() {
        when(productService.findByIdProduct(id)).thenReturn(null);

        assertThrows(ResourceNorFoundException.class, () ->
                productController.showProductById(id)
        );
    }

    @Test
    void saveProduct() throws Exception {
        when(productService.save(any(ProductDto.class))).thenReturn(productDto);

        ResponseEntity<Message> response = productController.saveProduct(productDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Saved Successfully", response.getBody().getNote());
        verify(productService, times(1)).save(any(ProductDto.class));
    }

    @Test
    void saveProductDataAccessException() {
        when(productService.save(any(ProductDto.class)))
                .thenThrow(new DataAccessException(MESSAGE_DB_ERROR) {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                productController.saveProduct(productDto)
        );

        assertEquals("Error save record: DB error", thrown.getMessage());
    }

    @Test
    void updateProduct() throws Exception {
        when(productService.existProduct(id)).thenReturn(true);
        when(productService.updateProduct(any(ProductDto.class), eq(id))).thenReturn(productDto);

        ResponseEntity<Message> response = productController.updateProduct(id, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Successfully", response.getBody().getNote());
        verify(productService, times(1)).updateProduct(any(ProductDto.class), eq(id));
    }

    @Test
    void updateProductNotFound() {
        when(productService.existProduct(id)).thenReturn(false);

        assertThrows(ResourceNorFoundException.class, () ->
                productController.updateProduct(id, productDto)
        );
    }

    @Test
    void updateProductDataAccessException() {
        when(productService.existProduct(id)).thenReturn(true);
        when(productService.updateProduct(any(ProductDto.class), eq(id)))
                .thenThrow(new DataAccessException(MESSAGE_DB_ERROR) {});

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                productController.updateProduct(id, productDto)
        );

        assertEquals("Error update record: DB error", thrown.getMessage());
    }

    @Test
    void deleteProduct() throws Exception {
        when(productService.existProduct(id)).thenReturn(true);
        doNothing().when(productService).deleteProduct(id);

        ResponseEntity<Message> response = productController.deleteProduct(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deleted Successfully", response.getBody().getNote());
        verify(productService, times(1)).deleteProduct(id);
    }

    @Test
    void deleteProductNotFound() {
        when(productService.existProduct(id)).thenReturn(false);

        assertThrows(ResourceNorFoundException.class, () ->
                productController.deleteProduct(id)
        );
    }

    @Test
    void deleteProductDataAccessException() {
        when(productService.existProduct(id)).thenReturn(true);
        doThrow(new DataAccessException(MESSAGE_DB_ERROR) {}).when(productService).deleteProduct(id);

        BadRequestException thrown = assertThrows(BadRequestException.class, () ->
                productController.deleteProduct(id)
        );

        assertEquals("Error delete record: DB error", thrown.getMessage());
    }
}
