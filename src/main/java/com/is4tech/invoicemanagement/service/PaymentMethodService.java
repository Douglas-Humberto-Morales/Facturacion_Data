package com.is4tech.invoicemanagement.service;

import com.is4tech.invoicemanagement.model.Product;
import com.is4tech.invoicemanagement.utils.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.PaymentMethodDto;
import com.is4tech.invoicemanagement.exception.BadRequestException;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.PaymentMethod;
import com.is4tech.invoicemanagement.repository.PaymentMethodRepository;
import com.is4tech.invoicemanagement.utils.MessagePage;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    
    private static final String NAME_ENTITY = "Payment Method";
    private static final String ID_ENTITY = "payment_method_id";

    public MessagePage findByAllPaymentMethod(Pageable pageable, HttpServletRequest request){
        Page<PaymentMethod> listAllPaymentMethod = paymentMethodRepository.findAll(pageable);

        /*
        if(listAllPaymentMethod.isEmpty()){
            int statusCode = HttpStatus.NOT_FOUND.value();
            auditService.logAudit(null, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, pageable.toString());
        }

        int statusCode = HttpStatus.OK.value();
        auditService.logAudit(listAllPaymentMethod.getContent(), this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        */

        return MessagePage.builder()
            .note("Reports Retrieved Successfully")
            .object(listAllPaymentMethod.getContent().stream().map(this::toDto).toList())
            .totalElements((int) listAllPaymentMethod.getTotalElements())
            .totalPages(listAllPaymentMethod.getTotalPages())
            .currentPage(listAllPaymentMethod.getNumber())
            .pageSize(listAllPaymentMethod.getSize())
            .build();
    }

    public Message findAllPaymentMethods() {
        List<PaymentMethod> listAllProducts = paymentMethodRepository.findAll();

        Message message = new Message();

        message.setNote("All Payment methods Retrieved Successfully");
        message.setObject(listAllProducts.stream().map(this::toDto).toList());

        return message;
    }

    public PaymentMethodDto findByIdPaymentMethodDto(Integer id, HttpServletRequest request){
        PaymentMethodDto paymentMethodDto = paymentMethodRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Payment Method not found"));
        //int statusCode = HttpStatus.OK.value();
        //auditService.logAudit(paymentMethodDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
        return paymentMethodDto;
    }

    public boolean existPaymentMethod(Integer id){
        return paymentMethodRepository.existsById(id);
    }

    public PaymentMethodDto savePaymentMethod(PaymentMethodDto paymentMethodDto, HttpServletRequest request){
        try {
            PaymentMethod paymentMethod = toModel(paymentMethodDto);
            PaymentMethod savePaymentMethod = paymentMethodRepository.save(paymentMethod);
            //int statusCode = HttpStatus.CREATED.value();
            //auditService.logAudit(reportDto, this.getClass().getMethods()[0], null, statusCode, NAME_ENTITY, request);
            return toDto(savePaymentMethod);
        } catch (Exception e) {
            //int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(reportDto, this.getClass().getMethods()[0], e, statusCode, NAME_ENTITY, request);
            throw e;
        }
    }

    public void deletePaymentMethod(PaymentMethodDto paymentMethodDto, HttpServletRequest request){
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodDto.getPaymentMethodId())
                    .orElseThrow(() -> new ResourceNorFoundException("Payment Method not found"));

            paymentMethodRepository.delete(paymentMethod);

            //int statusCode = HttpStatus.NO_CONTENT.value();
            //auditService.logAudit(paymentMethodDto, this.getClass().getMethods()[0], null, statusCode, "Payment Method", request);

        } catch (ResourceNorFoundException e) {
            int statusCode = HttpStatus.NOT_FOUND.value();
            //auditService.logAudit(paymentMethodDto, this.getClass().getMethods()[0], null, statusCode, "Payment Method", request);
            throw e;
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(paymentMethodDto, this.getClass().getMethods()[0], null, statusCode, "Payment Method", request);
            throw new BadRequestException("Unexpected error occurred: " + e.getMessage());
        }
    }

    public PaymentMethodDto updatePaymentMethodDto(PaymentMethodDto paymentMethodDto, Integer id, 
        HttpServletRequest request) throws BadRequestException{
        if (paymentMethodDto.getPaymentMethodId() == null) {
            throw new BadRequestException("Payment Method ID cannot be null");
        }

        try {    
            PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                    .orElseThrow(() -> new ResourceNorFoundException("Role not found"));

            if(paymentMethodDto.getName() != null){
                paymentMethod.setName(paymentMethodDto.getName());
            } 

            PaymentMethod updatePaymentMethod = paymentMethodRepository.save(paymentMethod);
            
            //int statusCode = HttpStatus.OK.value();
            //auditService.logAudit(paymentMethodDto, this.getClass().getMethods()[0], null, statusCode, "Payment Method", request);

            return toDto(updatePaymentMethod);
        } catch (Exception e) {
            int statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
            //auditService.logAudit(paymentMethodDto, this.getClass().getMethods()[0], e, statusCode, "Payment Method", request);
            throw new BadRequestException("Error updating role: " + e.getMessage());
        }
    }

    private PaymentMethod toModel(PaymentMethodDto paymentMethodDto){
        return PaymentMethod.builder()
            .paymentMethodId(paymentMethodDto.getPaymentMethodId())
            .name(paymentMethodDto.getName())
            .build();
    }

    private PaymentMethodDto toDto(PaymentMethod paymentMethod){
        return PaymentMethodDto.builder()
            .paymentMethodId(paymentMethod.getPaymentMethodId())
            .name(paymentMethod.getName())
            .build();
    }
}
