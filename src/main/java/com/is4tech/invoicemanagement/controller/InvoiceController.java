package com.is4tech.invoicemanagement.controller;

import com.is4tech.invoicemanagement.dto.InvoiceDto;
import com.is4tech.invoicemanagement.exception.BadRequestException;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.InvoiceService;
import com.is4tech.invoicemanagement.utils.JwtUtil;
import com.is4tech.invoicemanagement.utils.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice-management/v0.1/")
public class InvoiceController {
    private final InvoiceService invoiceService;

    private static final String NAME_ENTITY = "Invoice";
    private static final String ID_ENTITY = "invoice_id";

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public ResponseEntity<Message> showAllInvoices(@PageableDefault(size = 10) Pageable pageable) {
        List<InvoiceDto> listInvoice = invoiceService.findByAllInvoice(pageable);
        if (listInvoice.isEmpty())
            throw new ResourceNorFoundException(NAME_ENTITY);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listInvoice)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity<Message> showByIdInvoice(@PathVariable Integer id) {
        InvoiceDto invoiceDto = invoiceService.findByIdInvoice(id);

        if (invoiceDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(invoiceDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/invoice")
    public ResponseEntity<Message> saveInvoice(
            @RequestBody @Valid InvoiceDto invoiceDto, HttpServletRequest request) throws BadRequestException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new BadRequestException("Authorization token is missing or invalid.");
            }
            String token = authorizationHeader.substring(7);

            String userId = JwtUtil.extractUserIdFromToken(token);

            invoiceDto.setUserId(Integer.valueOf(userId));

            InvoiceDto saveInvoice = invoiceService.saveInvoice(invoiceDto, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(saveInvoice)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/invoice/status/{id}")
    public ResponseEntity<Message> changeStatusInvoice(
            @RequestBody @Valid InvoiceDto invoiceDto,
            @PathVariable Integer id) throws BadRequestException {
        try {
            if (invoiceService.existInvoice(id)) {
                invoiceService.changeStatusInvoice(invoiceDto, id);
                return new ResponseEntity<>(Message.builder()
                        .note("Invoice change status successfully")
                        .object(null)
                        .build(),
                        HttpStatus.CREATED);
            } else
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error update record: " + exDt.getMessage());
        }
    }
}
