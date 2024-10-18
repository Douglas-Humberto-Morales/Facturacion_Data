package com.is4tech.invoicemanagement.controller;

import com.is4tech.invoicemanagement.service.InvoiceService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoice-management/v0.1/invoice/")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/show-all-invoices")
    public ResponseEntity<Message> showAllInvoices(@PageableDefault(size = 10)Pageable pageable) {
        MessagePage listInvoices = invoiceService.findByAllInvoices(pageable);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listInvoices)
                .build(),
        HttpStatus.OK);
    }
}
