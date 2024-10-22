package com.is4tech.invoicemanagement.controller;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.dto.InvoiceDto;
import com.is4tech.invoicemanagement.dto.InvoiceProductDetailDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.DetailInvoiceProducts;
import com.is4tech.invoicemanagement.model.Invoice;
import com.is4tech.invoicemanagement.service.DetailInvoiceProductsService;
import com.is4tech.invoicemanagement.service.InvoiceService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;
import com.is4tech.invoicemanagement.utils.SendEmail;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice-management/v0.1/detail-invoice-product")
public class DetailInvoiceProductsController {
    private final DetailInvoiceProductsService detailInvoiceProductsService;
    private final InvoiceService invoiceService;
    private final SendEmail sendEmail;

    public DetailInvoiceProductsController(DetailInvoiceProductsService detailInvoiceProductsService, SendEmail sendEmail,
        InvoiceService invoiceService) {
        this.detailInvoiceProductsService = detailInvoiceProductsService;
        this.sendEmail = sendEmail;
        this.invoiceService = invoiceService;
    }

    private static final String NAME_ENTITY = "Detail Invoice Products";
    private static final String ID_ENTITY = "detail_invoice_products_id";

    @GetMapping("/show-all")
    public ResponseEntity<Message> showAllDetailInvoiceProducts(@PageableDefault(size = 10) Pageable pageable,
            HttpServletRequest request) {
        MessagePage listDetailInvoiceProducts = detailInvoiceProductsService
                .findByDetailInvoiceProduct(pageable);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listDetailInvoiceProducts)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/show-by-id/{id}")
    public ResponseEntity<Message> showByIdDetailInvoiceProducts(@PathVariable Integer id,
    HttpServletRequest request) {
        DetailInvoiceProductsDto detailInvoiceProductsDto = detailInvoiceProductsService
                .findByIdDetailInvoiceProducts(id);
        if (detailInvoiceProductsDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(detailInvoiceProductsDto)
                .build(),
                HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Message> saveDetailInvoiceProducts(
            @RequestBody @Valid List<DetailInvoiceProductsDto> detailInvoiceProductsDtos,
            HttpServletRequest request) throws BadRequestException {
        try {
            List<DetailInvoiceProductsDto> savedDetails = new ArrayList<>();

            for (DetailInvoiceProductsDto detailInvoiceProductsDto : detailInvoiceProductsDtos) {
                DetailInvoiceProductsDto savedDetail = detailInvoiceProductsService
                        .saveDetailInvoiceProducts(detailInvoiceProductsDto, request);
                savedDetails.add(savedDetail);
            }

            return new ResponseEntity<>(Message.builder()
                    .note("All records saved successfully")
                    .object(savedDetails)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error saving records: " + exDt.getMessage());
        }
    }

    @GetMapping("/invoice/{id}/pdf")
    public ResponseEntity<byte[]> generateInvoicePdf(@PathVariable Integer id) {
        try {
        InvoiceDto invoice = invoiceService.findByIdInvoice(id);

        List<DetailInvoiceProductsDto> products = detailInvoiceProductsService.findByIdInvoiceDetailInvoiceProducts(id);

        

        List<InvoiceProductDetailDto> productDetails = products.stream()
            .map(product -> toDto(invoice, product))
            .toList();
        byte[] pdfContent = detailInvoiceProductsService.exportInvoiceReport(productDetails);

        String destination = "facturacion@gmail.com";
        String from = "facturacion@gmail.com";
        String subject = "Reporte PDF";

        sendEmail.sendEmailWithPdf(destination, from, subject, pdfContent);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "invoice_" + id + ".pdf");

        return ResponseEntity.ok().headers(headers).body(pdfContent);
        
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error en la URL: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error generando o enviando el reporte PDF: " + e.getMessage());
        }
    }

    private InvoiceProductDetailDto toDto(InvoiceDto invoice, DetailInvoiceProductsDto product) {
        return InvoiceProductDetailDto.builder()
            .detailInvoiceProductsId(product.getInvoiceId())
            .name(product.getName())
            .price(product.getPrice())
            .amount(product.getAmount())
            .totalPrice(product.getAmount() * product.getPrice())
            .creationDate((invoice.getCreationDate() != null) ? invoice.getCreationDate() : new Date())
            .subtotal(invoice.getSubtotal())
            .total(invoice.getTotal())
            .build();
    }
}
