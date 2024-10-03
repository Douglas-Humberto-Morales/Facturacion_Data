package com.is4tech.invoicemanagement.controller;

import java.util.List;

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

import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.DetailInvoiceProductsService;
import com.is4tech.invoicemanagement.utils.Message;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice-management/v0.1/")
public class DetailInvoiceProductsController {
    private final DetailInvoiceProductsService detailInvoiceProductsService;

    public DetailInvoiceProductsController(DetailInvoiceProductsService detailInvoiceProductsService) {
        this.detailInvoiceProductsService = detailInvoiceProductsService;
    }

    private static final String NAME_ENTITY = "Detail Invoice Products";
    private static final String ID_ENTITY = "detail_invoice_products_id";

    @GetMapping("/details-invoice-products")
    public ResponseEntity<Message> showAllDetailInvoiceProducts(@PageableDefault(size = 10) Pageable pageable) {
        List<DetailInvoiceProductsDto> listDetailInvoiceProducts = detailInvoiceProductsService
                .findByDetailInvoiceProduct(pageable);
        if (listDetailInvoiceProducts.isEmpty())
            throw new ResourceNorFoundException(NAME_ENTITY);

        return new ResponseEntity<>(Message.builder()
                .note("Records found")
                .object(listDetailInvoiceProducts)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/detail-invoice-products/{id}")
    public ResponseEntity<Message> showByIdDetailInvoiceProducts(@PathVariable Integer id) {
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

    @PostMapping("/detail-invoice-products")
    public ResponseEntity<Message> saveDetailInvoiceProducts(
            @RequestBody @Valid DetailInvoiceProductsDto detailInvoiceProductsDto) throws BadRequestException {
        try {
            DetailInvoiceProductsDto saveDetailInvoiceProductsDto = detailInvoiceProductsService
                    .saveDetailInvoiceProducts(detailInvoiceProductsDto);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved successfully")
                    .object(saveDetailInvoiceProductsDto)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error save record: " + exDt.getMessage());
        }
    }

    @PutMapping("/detail-invoice-products/{id}")
    public ResponseEntity<Message> updateDetailInvoiceProducts(
            @RequestBody @Valid DetailInvoiceProductsDto paymentMethodDto,
            @PathVariable Integer id) throws BadRequestException {
        try {
            if (detailInvoiceProductsService.existDetailInvoiceProducts(id)) {
                DetailInvoiceProductsDto updateDetailInvoiceProductsDto = detailInvoiceProductsService
                        .updateDetailInvoiceProducts(paymentMethodDto, id);
                return new ResponseEntity<>(Message.builder()
                        .note("Saved successfully")
                        .object(updateDetailInvoiceProductsDto)
                        .build(),
                        HttpStatus.CREATED);
            } else
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error update record: " + exDt.getMessage());
        }
    }

    @DeleteMapping("/detail-invoice-products/{id}")
    public ResponseEntity<Message> deleteDetailInvoiceProducts(@PathVariable Integer id) throws BadRequestException {
        try {
            detailInvoiceProductsService.deleteDetailInvoiceProduct(id);
            return new ResponseEntity<>(Message.builder()
                    .object(null)
                    .build(),
                    HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            throw new BadRequestException("Error delete record: " + exDt.getMessage());
        }
    }
}