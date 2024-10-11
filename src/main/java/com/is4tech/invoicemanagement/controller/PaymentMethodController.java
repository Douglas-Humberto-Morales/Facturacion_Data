package com.is4tech.invoicemanagement.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
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

import com.is4tech.invoicemanagement.dto.PaymentMethodDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.PaymentMethodService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice-management/v0.1/")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    private static final String NAME_ENTITY = "Payment Method";
    private static final String ID_ENTITY = "payment_method_id";

    @GetMapping("/payment-methods")
    public ResponseEntity<Message> showAllInvoiceStatements(@PageableDefault(size = 10) Pageable pageable,
        HttpServletRequest request) {
        MessagePage listPaymentMethod = paymentMethodService.findByAllPaymentMethod(pageable, request);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listPaymentMethod)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/payment-method/{id}")
    public ResponseEntity<Message> showByIdPaymentMethod (@PathVariable Integer id, HttpServletRequest request) {
        PaymentMethodDto paymentMethodDto = paymentMethodService.findByIdPaymentMethodDto(id, request);
        if (paymentMethodDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(paymentMethodDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/payment-method")
    public ResponseEntity<Message> savePaymentMethod(@RequestBody @Valid PaymentMethodDto paymentMethodDto,
        HttpServletRequest request) throws BadRequestException {
        try {
            PaymentMethodDto savePaymentMethodDto = paymentMethodService.savePaymentMethod(paymentMethodDto, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(savePaymentMethodDto)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/payment-method/{id}")
    public ResponseEntity<Message> updatePaymentMethod(@RequestBody @Valid PaymentMethodDto paymentMethodDto,
            @PathVariable Integer id, HttpServletRequest request) throws BadRequestException {
        PaymentMethodDto updatePaymentMethodDto = null;
        try {
            if (!paymentMethodService.existPaymentMethod(id)) {
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
            }
            paymentMethodDto.setPaymentMethodId(id);
            updatePaymentMethodDto = paymentMethodService.updatePaymentMethodDto(paymentMethodDto, id, request);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(updatePaymentMethodDto)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error updating record: " + exDt.getMessage());
        } catch (Exception e) {
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/payment-method/{id}")
    public ResponseEntity<Message> deletePaymentMethod(@PathVariable Integer id, 
    HttpServletRequest request) throws BadRequestException {
        try {
            PaymentMethodDto paymentMethodDto = paymentMethodService.findByIdPaymentMethodDto(id, request);
            paymentMethodService.deletePaymentMethod(paymentMethodDto, request);
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
}
