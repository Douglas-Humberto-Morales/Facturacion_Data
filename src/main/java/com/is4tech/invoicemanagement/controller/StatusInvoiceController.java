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

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;

import com.is4tech.invoicemanagement.dto.PaymentMethodDto;
import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.StatusInvoiceService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice-management/v0.1/status-invoice")
public class StatusInvoiceController {

    private final StatusInvoiceService statusInvoiceService;

    public StatusInvoiceController(StatusInvoiceService statusInvoiceService) {
        this.statusInvoiceService = statusInvoiceService;
    }

    private static final String NAME_ENTITY = "Status Invoice";
    private static final String ID_ENTITY = "status_invoice_id";

    @GetMapping("/show-all")
    public ResponseEntity<Message> showAllInvoiceStatements(@PageableDefault(size = 10) Pageable pageable, 
    HttpServletRequest request) {
        MessagePage listStatusInvoice = statusInvoiceService.listAllStatusInvoice(pageable, request);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listStatusInvoice)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/show-by-id/{id}")
    public ResponseEntity<Message> showByIdStatusInvoice(@PathVariable Integer id, HttpServletRequest request) {
        StatusInvoiceDto statusInvoiceDto = statusInvoiceService.findByIdStatusInvoice(id, request);
        if (statusInvoiceDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(statusInvoiceDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Message> saveStatusInvoice(@RequestBody @Valid StatusInvoiceDto statusInvoiceDto,
        HttpServletRequest request) throws BadRequestException {
        try { 
            StatusInvoiceDto saveStatusInvoiceDto = statusInvoiceService.saveStatusInvoice(statusInvoiceDto, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(saveStatusInvoiceDto)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Message> updateStatusInvoice(@RequestBody @Valid StatusInvoiceDto statusInvoiceDto,
            @PathVariable Integer id, HttpServletRequest request) throws BadRequestException {
        StatusInvoiceDto statusInvoice = null;
        try {
            if (!statusInvoiceService.existStatusInvoice(id)) {
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
            }
            statusInvoiceDto.setStatudInvoiceId(id);
            statusInvoice = statusInvoiceService.updateStatusInvoice(id, statusInvoiceDto, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(statusInvoice)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error updating record: " + exDt.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Message> deleteStatusInvoice(@PathVariable Integer id, 
    HttpServletRequest request)throws BadRequestException {
        try {
            StatusInvoiceDto statusInvoiceDto = statusInvoiceService.findByIdStatusInvoice(id, request);
            statusInvoiceService.deleteStatusInvoice(statusInvoiceDto, request);
            return new ResponseEntity<>(Message.builder()
                    .object(null)
                    .build(),
                    HttpStatus.NO_CONTENT);
        } catch (ResourceNorFoundException e) {
            throw new BadRequestException("Rol not found: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new BadRequestException("Error deleting record: " + e.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/show-all-not-pageable")
    public ResponseEntity<Message> showAllStatusInvoiceNotPag () {
        List<StatusInvoiceDto> listStatusInvoice = statusInvoiceService.findByAllPaymentMethodNotPageable();

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listStatusInvoice)
                .build(),
                HttpStatus.OK);
    }
}
