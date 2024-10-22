package com.is4tech.invoicemanagement.service;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.StatusInvoice;
import com.is4tech.invoicemanagement.repository.StatusInvoiceRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatusInvoiceService {
    private final StatusInvoiceRepository statusInvoiceRepository;
    
    private static final String NAME_ENTITY = "Status Invoice";
    private static final String ID_ENTITY = "status_invoice_id";

    public MessagePage listAllStatusInvoice(Pageable pageable, HttpServletRequest request){

        Page<StatusInvoice> listAllStatusInvoice = statusInvoiceRepository.findAll(pageable);

        if(listAllStatusInvoice.isEmpty()){
            //int statusCode = HttpStatus.NOT_FOUND.value();
            //auditService.logAudit(null, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, pageable.toString());
        }
        /*
        int statusCode = HttpStatus.OK.value();
        auditService.logAudit(listAllStatusInvoice.getContent(), this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        */

        return MessagePage.builder()
            .note("Reports Retrieved Successfully")
            .object(listAllStatusInvoice.getContent().stream().map(this::toDto).toList())
            .totalElements((int) listAllStatusInvoice.getTotalElements())
            .totalPages(listAllStatusInvoice.getTotalPages())
            .currentPage(listAllStatusInvoice.getNumber())
            .pageSize(listAllStatusInvoice.getSize())
            .build();
    }

    public List<StatusInvoiceDto> findByAllPaymentMethodNotPageable(){

        List<StatusInvoiceDto> listAllStatusInvoice = statusInvoiceRepository.findAll().stream()
            .map(this::toDto).toList();

        if(listAllStatusInvoice.isEmpty()){
            //int statusCode = HttpStatus.NOT_FOUND.value();
            //auditService.logAudit(null, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            throw new ResourceNorFoundException(NAME_ENTITY);
        }

        /*
        int statusCode = HttpStatus.OK.value();
        auditService.logAudit(listAllStatusInvoice.getContent(), this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        */

        return listAllStatusInvoice;
    }

    public StatusInvoiceDto findByIdStatusInvoice(Integer id, HttpServletRequest request){
        StatusInvoiceDto statusInvoiceDto = statusInvoiceRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Status Invoice not found"));
        //int statusCode = HttpStatus.OK.value();
        //auditService.logAudit(statusInvoiceDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        return statusInvoiceDto;
    }

    public boolean existStatusInvoice(Integer id){
        return statusInvoiceRepository.existsById(id);
    }

    public StatusInvoiceDto saveStatusInvoice(StatusInvoiceDto statusInvoicedDto, HttpServletRequest request){
        try {
            StatusInvoice statusInvoice = toModel(statusInvoicedDto);
            StatusInvoice statusInvoiceSave = statusInvoiceRepository.save(statusInvoice);
            //int statusCode = HttpStatus.CREATED.value();
            //auditService.logAudit(reportDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            return toDto(statusInvoiceSave);
        } catch (Exception e) {
            //int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(reportDto, this.getClass().getMethods()[0], e, statusCode, NAME_ENTITY, request);
            throw e;
        }
    }

    public void deleteStatusInvoice(StatusInvoiceDto statusInvoiceDto, HttpServletRequest request) 
    throws BadRequestException{
        try {
            StatusInvoice deleteStatusInvoice = statusInvoiceRepository.findById(statusInvoiceDto.getStatudInvoiceId())
                .orElse(null);
            assert deleteStatusInvoice != null;
            statusInvoiceRepository.delete(deleteStatusInvoice);
            int statusCode = HttpStatus.NO_CONTENT.value();
            //auditService.logAudit(statusInvoiceDto, this.getClass().getMethods()[0], null, statusCode, "Status Invoice", request);
        } catch (ResourceNorFoundException e) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            //auditService.logAudit(statusInvoiceDto, this.getClass().getMethods()[0], null, statusCode, "Status Invoice", request);
            throw e;

        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(statusInvoiceDto, this.getClass().getMethods()[0], null, statusCode, "Status Invoice", request);
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    public StatusInvoiceDto updateStatusInvoice(Integer id, StatusInvoiceDto statusInvoideDto,
        HttpServletRequest request) throws BadRequestException{
        if (statusInvoideDto.getStatudInvoiceId() == null) {
            throw new BadRequestException("Status Invoice ID cannot be null");
        }
        try {
            StatusInvoice statusInvoice = statusInvoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNorFoundException("Role not found"));

            if(statusInvoideDto.getName() != null){
                statusInvoice.setName(statusInvoideDto.getName());
            }

            StatusInvoice updateStatusInvoice = statusInvoiceRepository.save(statusInvoice);
            
            int statusCode = HttpStatus.OK.value();
            //auditService.logAudit(statusInvoideDto, this.getClass().getMethods()[0], null, statusCode, "Status Invoice", request);

            return toDto(updateStatusInvoice);
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(statusInvoideDto, this.getClass().getMethods()[0], e, statusCode, "Status Invoice", request);
            throw new BadRequestException("Error updating status invoice: " + e.getMessage());
        }
    }

    private StatusInvoiceDto toDto(StatusInvoice statusInvoice){
        return StatusInvoiceDto.builder()
            .statudInvoiceId(statusInvoice.getStatusInvoiceId())
            .name(statusInvoice.getName())
            .build();
    }

    private StatusInvoice toModel(StatusInvoiceDto statusInvoiceDto){
        return StatusInvoice.builder()
            .statusInvoiceId(statusInvoiceDto.getStatudInvoiceId())
            .name(statusInvoiceDto.getName())
            .build();
    }
}
