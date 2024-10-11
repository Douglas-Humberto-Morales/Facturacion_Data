package com.is4tech.invoicemanagement.service;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.DetailInvoiceProducts;
import com.is4tech.invoicemanagement.repository.DetailInvoiceProductsRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DetailInvoiceProductsService {
    private final DetailInvoiceProductsRepository detailInvoiceProductsRepository;
    
    private static final String NAME_ENTITY = "Detail Invoice Products";
    private static final String ID_ENTITY = "detail_invoice_products_id";

    public MessagePage findByDetailInvoiceProduct(Pageable pageable, HttpServletRequest request){
        Page<DetailInvoiceProducts> lsitaAlldetailInvoiceProducts = detailInvoiceProductsRepository.findAll(pageable);

        /*
        if(lsitaAlldetailInvoiceProducts.isEmpty()){
            int statusCode = HttpStatus.NOT_FOUND.value();
            auditService.logAudit(null, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, pageable.toString());
        }

        int statusCode = HttpStatus.OK.value();
        auditService.logAudit(lsitaAlldetailInvoiceProducts.getContent(), this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        */

        return MessagePage.builder()
            .note("Reports Retrieved Successfully")
            .object(lsitaAlldetailInvoiceProducts.getContent().stream().map(this::toDto).toList())
            .totalElements((int) lsitaAlldetailInvoiceProducts.getTotalElements())
            .totalPages(lsitaAlldetailInvoiceProducts.getTotalPages())
            .currentPage(lsitaAlldetailInvoiceProducts.getNumber())
            .pageSize(lsitaAlldetailInvoiceProducts.getSize())
            .build();
    }

    public DetailInvoiceProductsDto findByIdDetailInvoiceProducts(Integer id, HttpServletRequest request){
        DetailInvoiceProductsDto detailInvoiceProductsDto = detailInvoiceProductsRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Detail Invoice Products not found"));
        //int statusCode = HttpStatus.OK.value();
        //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        return detailInvoiceProductsDto;
    }

    public boolean existDetailInvoiceProducts(Integer id){
        return detailInvoiceProductsRepository.existsById(id);
    }

    public DetailInvoiceProductsDto saveDetailInvoiceProducts(DetailInvoiceProductsDto detailInvoiceProductsDto,
        HttpServletRequest request){
        try {
            DetailInvoiceProducts detailInvoiceProducts = toModel(detailInvoiceProductsDto);
            DetailInvoiceProducts saveDetailInvoiceProducts = detailInvoiceProductsRepository.save(detailInvoiceProducts);
            //int statusCode = HttpStatus.CREATED.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            return toDto(saveDetailInvoiceProducts);
        } catch (Exception e) {
            //int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], e, statusCode, NAME_ENTITY, request);
            throw e;
        }
    }

    public void deleteDetailInvoiceProduct(DetailInvoiceProductsDto detailInvoiceProductsDto, 
    HttpServletRequest request) throws BadRequestException{
        try {
            DetailInvoiceProducts deleteDetailInvoiceProducts = detailInvoiceProductsRepository
                .findById(detailInvoiceProductsDto.getDetailInvoiceProductsId())
                .orElseThrow(() -> new ResourceNorFoundException("Detail Invoice Product not found"));
            detailInvoiceProductsRepository.delete(deleteDetailInvoiceProducts);

            //int statusCode = HttpStatus.NO_CONTENT.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], null, statusCode, "Detail Invoice Product", request);

        } catch (ResourceNorFoundException e) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], null, statusCode, "Detail Invoice Product", request);
            throw e;
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], null, statusCode, "Detail Invoice Product", request);
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    public DetailInvoiceProductsDto updateDetailInvoiceProducts(DetailInvoiceProductsDto detailInvoiceProductsDto, Integer id,
        HttpServletRequest request) throws BadRequestException{
        
        if (detailInvoiceProductsDto.getDetailInvoiceProductsId() == null) {
            throw new BadRequestException("Detail Invoice Products ID cannot be null");
        }

        try {
            DetailInvoiceProducts detailInvoiceProducts = detailInvoiceProductsRepository.findById(id)
                    .orElseThrow(() -> new ResourceNorFoundException("Detail Invoice Products not found"));

            if (detailInvoiceProductsDto.getName() != null) {
                detailInvoiceProducts.setName(detailInvoiceProductsDto.getName());
            }
            if (detailInvoiceProductsDto.getPrice() != null) {
                detailInvoiceProducts.setPrice(detailInvoiceProductsDto.getPrice());
            }

            DetailInvoiceProducts saveDetailInvoiceProducts = detailInvoiceProductsRepository.save(detailInvoiceProducts);

            //int statusCode = HttpStatus.OK.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], null, statusCode, "Detail Invoice Products", request);

            return toDto(detailInvoiceProducts);
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(detailInvoiceProductsDto, this.getClass().getMethods()[0], e, statusCode, "Detail Invoice Products", request);
            throw new BadRequestException("Error updating Detail Invoice Products: " + e.getMessage());
        }
    }

    private DetailInvoiceProducts toModel(DetailInvoiceProductsDto detailInvoiceProductsDto){
        return DetailInvoiceProducts.builder()
            .detailInvoiceProductsId(detailInvoiceProductsDto.getDetailInvoiceProductsId())
            .name(detailInvoiceProductsDto.getName())
            .price(detailInvoiceProductsDto.getPrice())
            .build();
    }

    private DetailInvoiceProductsDto toDto(DetailInvoiceProducts detailInvoiceProducts){
        return DetailInvoiceProductsDto.builder()
            .detailInvoiceProductsId(detailInvoiceProducts.getDetailInvoiceProductsId())
            .name(detailInvoiceProducts.getName())
            .price(detailInvoiceProducts.getPrice())
            .build();
    }
}
