package com.is4tech.invoicemanagement.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.DetailInvoiceProductsService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/invoice-management/v0.1/detail-invoice-product")
public class DetailInvoiceProductsController {
    private final DetailInvoiceProductsService detailInvoiceProductsService;

    public DetailInvoiceProductsController(DetailInvoiceProductsService detailInvoiceProductsService) {
        this.detailInvoiceProductsService = detailInvoiceProductsService;
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

}
