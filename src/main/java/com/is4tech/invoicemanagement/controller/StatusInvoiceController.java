package com.is4tech.invoicemanagement.controller;

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
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import java.util.List;

import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.StatusInvoiceService;
import com.is4tech.invoicemanagement.utils.Message;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice-management/v0.1")
public class StatusInvoiceController {

    private final StatusInvoiceService statusInvoiceService;

    public StatusInvoiceController(StatusInvoiceService statusInvoiceService) {
        this.statusInvoiceService = statusInvoiceService;
    }

    private static final String NAME_ENTITY = "Status Invoice";
    private static final String ID_ENTITY = "status_invoice_id";

    @GetMapping("/status-invoices")
    public ResponseEntity<Message> showAllInvoiceStatements(@PageableDefault(size = 10) Pageable pageable) {
        List<StatusInvoiceDto> listStatusInvoice = statusInvoiceService.listAllStatusInvoice(pageable);
        if (listStatusInvoice.isEmpty())
            throw new ResourceNorFoundException(NAME_ENTITY);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listStatusInvoice)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/status-invoice/{id}")
    public ResponseEntity<Message> showByIdStatusInvoice(@PathVariable Integer id) {
        StatusInvoiceDto statusInvoiceDto = statusInvoiceService.findByIdStatusInvoice(id);
        if (statusInvoiceDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(statusInvoiceDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/status-invoice")
    public ResponseEntity<Message> saveStatusInvoice(@RequestBody @Valid StatusInvoiceDto statusInvoiceDto)
            throws BadRequestException {
        try { 
            StatusInvoiceDto saveStatusInvoiceDto = statusInvoiceService.saveStatusInvoice(statusInvoiceDto);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(saveStatusInvoiceDto)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/status-invoice/{id}")
    public ResponseEntity<Message> updateStatusInvoice(@RequestBody @Valid StatusInvoiceDto statusInvoiceDto,
            @PathVariable Integer id) throws BadRequestException {
        try {
            if (statusInvoiceService.existStatusInvoice(id)) {
                StatusInvoiceDto updateStatusInvoiceDto = statusInvoiceService.updateStatusInvoice(statusInvoiceDto, id);
                return new ResponseEntity<>(Message.builder()
                        .note("Saved successfully")
                        .object(updateStatusInvoiceDto)
                        .build(),
                        HttpStatus.CREATED);
            } else
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error update record: " + exDt.getMessage());
        }
    }

    @DeleteMapping("/status-invoice/{id}")
    public ResponseEntity<Message> deleteStatusInvoice(@PathVariable Integer id) throws BadRequestException {
        try {
            statusInvoiceService.deleteStatusInvoice(id);
            return new ResponseEntity<>(Message.builder()
                    .object(null)
                    .build(),
                    HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error delete record: " + exDt.getMessage());
        }
    }
}
