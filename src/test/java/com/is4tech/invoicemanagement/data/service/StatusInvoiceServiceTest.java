package com.is4tech.invoicemanagement.data.service;

import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.StatusInvoice;
import com.is4tech.invoicemanagement.repository.StatusInvoiceRepository;
import com.is4tech.invoicemanagement.service.StatusInvoiceService;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StatusInvoiceServiceTest {

    @Mock
    private StatusInvoiceRepository statusInvoiceRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private StatusInvoiceService statusInvoiceService;

    private StatusInvoiceDto statusInvoiceDto;
    private StatusInvoice statusInvoice;
    private int id = 1;

    private static final String MESSAJE_DB_ERROR = "DB error";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statusInvoiceDto = StatusInvoiceDto.builder()
                .statudInvoiceId(id)
                .name("Activo")
                .build();

        statusInvoice = StatusInvoice.builder()
                .statusInvoiceId(id)
                .name("Activo")
                .build();
    }

    @Test
    void listAllStatusInvoice() {
        Pageable pageable = PageRequest.of(0, 10);
        List<StatusInvoice> statusInvoiceList = new ArrayList<>();
        statusInvoiceList.add(statusInvoice);
        Page<StatusInvoice> statusInvoicePage = new PageImpl<>(statusInvoiceList);

        when(statusInvoiceRepository.findAll(pageable)).thenReturn(statusInvoicePage);

        MessagePage result = statusInvoiceService.listAllStatusInvoice(pageable, request);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(statusInvoiceRepository, times(1)).findAll(pageable);
    }

    @Test
    void listAllStatusInvoiceEmpty() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StatusInvoice> emptyPage = Page.empty(pageable);

        when(statusInvoiceRepository.findAll(pageable)).thenReturn(emptyPage);

        assertThrows(ResourceNorFoundException.class,
                () -> statusInvoiceService.listAllStatusInvoice(pageable, request));
    }

    @Test
    void findByAllPaymentMethodNotPageable() {
        List<StatusInvoice> statusInvoiceList = new ArrayList<>();
        statusInvoiceList.add(statusInvoice);

        when(statusInvoiceRepository.findAll()).thenReturn(statusInvoiceList);

        List<StatusInvoiceDto> result = statusInvoiceService.findByAllPaymentMethodNotPageable();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(statusInvoiceRepository, times(1)).findAll();
    }

    @Test
    void findByAllPaymentMethodNotPageableEmpty() {
        when(statusInvoiceRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(ResourceNorFoundException.class, () -> statusInvoiceService.findByAllPaymentMethodNotPageable());
    }

    @Test
    void findByIdStatusInvoice() {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.of(statusInvoice));

        StatusInvoiceDto result = statusInvoiceService.findByIdStatusInvoice(id, request);

        assertNotNull(result);
        assertEquals(statusInvoiceDto.getStatudInvoiceId(), result.getStatudInvoiceId());
        verify(statusInvoiceRepository, times(1)).findById(id);
    }

    @Test
    void findByIdStatusInvoiceNotFound() {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> statusInvoiceService.findByIdStatusInvoice(id, request));
    }

    @Test
    void saveStatusInvoice() {
        when(statusInvoiceRepository.save(any(StatusInvoice.class))).thenReturn(statusInvoice);

        StatusInvoiceDto result = statusInvoiceService.saveStatusInvoice(statusInvoiceDto, request);

        assertNotNull(result);
        assertEquals(statusInvoiceDto.getName(), result.getName());
        verify(statusInvoiceRepository, times(1)).save(any(StatusInvoice.class));
    }

    @Test
    void saveStatusInvoiceThrowsException() {
        when(statusInvoiceRepository.save(any(StatusInvoice.class)))
                .thenThrow(new RuntimeException(MESSAJE_DB_ERROR));

        assertThrows(RuntimeException.class, () -> statusInvoiceService.saveStatusInvoice(statusInvoiceDto, request));
    }

    @Test
    void deleteStatusInvoice() throws BadRequestException {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.of(statusInvoice));

        doAnswer(invocation -> {
            StatusInvoice entity = invocation.getArgument(0);
            assertNotNull(entity);
            return null;
        }).when(statusInvoiceRepository).delete(any(StatusInvoice.class));

        statusInvoiceService.deleteStatusInvoice(statusInvoiceDto, request);

        verify(statusInvoiceRepository, times(1)).delete(any(StatusInvoice.class));
    }

    @Test
    void deleteStatusInvoiceThrowsException() {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.of(statusInvoice));
        doThrow(new RuntimeException(MESSAJE_DB_ERROR)).when(statusInvoiceRepository).delete(any(StatusInvoice.class));

        assertThrows(BadRequestException.class,
                () -> statusInvoiceService.deleteStatusInvoice(statusInvoiceDto, request));
    }

    @Test
    void updateStatusInvoice() throws BadRequestException {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.of(statusInvoice));
        when(statusInvoiceRepository.save(any(StatusInvoice.class))).thenReturn(statusInvoice);

        StatusInvoiceDto updatedDto = StatusInvoiceDto.builder()
                .statudInvoiceId(id)
                .name("Nuevo Nombre")
                .build();

        StatusInvoiceDto result = statusInvoiceService.updateStatusInvoice(id, updatedDto, request);

        assertNotNull(result);
        assertEquals(updatedDto.getName(), result.getName()); // Asegura que el nombre fue cambiado
        verify(statusInvoiceRepository, times(1)).save(any(StatusInvoice.class));
    }

    @Test
    void updateStatusInvoiceNotFound() {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> statusInvoiceService.updateStatusInvoice(id, statusInvoiceDto, request));
    }

    @Test
    void updateStatusInvoiceThrowsException() {
        when(statusInvoiceRepository.findById(id)).thenReturn(Optional.of(statusInvoice));
        when(statusInvoiceRepository.save(any(StatusInvoice.class)))
                .thenThrow(new RuntimeException(MESSAJE_DB_ERROR));

        assertThrows(BadRequestException.class,
                () -> statusInvoiceService.updateStatusInvoice(id, statusInvoiceDto, request));
    }
}
