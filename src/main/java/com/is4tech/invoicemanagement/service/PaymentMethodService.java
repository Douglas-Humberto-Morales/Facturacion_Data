package com.is4tech.invoicemanagement.service;

import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.PaymentMethodDto;
import com.is4tech.invoicemanagement.model.PaymentMethod;
import com.is4tech.invoicemanagement.repository.PaymentMethodRepository;

import lombok.AllArgsConstructor;

import java.util.List;

@Service
@AllArgsConstructor
public class PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethodDto> findByPaymentMethod(){
        return null;
    }

    private PaymentMethod toModel(PaymentMethodDto paymentMethodDto){
        return PaymentMethod.builder()
            .paymentMethodId(paymentMethodDto.getPaymentMethodId())
            .name(paymentMethodDto.getName())
            .build();
    }

    private PaymentMethodDto toDto(PaymentMethod paymentMethod){
        return new PaymentMethodDto(paymentMethod.getPaymentMethodId(),paymentMethod.getName());
    }
}
