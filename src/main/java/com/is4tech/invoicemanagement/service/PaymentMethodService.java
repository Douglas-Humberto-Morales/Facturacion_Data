package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Pageable;
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

    public List<PaymentMethodDto> findByAllPaymentMethod(Pageable pageable){
        return paymentMethodRepository.findAll(pageable).stream()
            .map(this::toDto)
            .toList();
    }

    public PaymentMethodDto findByIdPaymentMethodDto(Integer id){
        return paymentMethodRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Payment Method not found"));
    }

    public boolean existPaymentMethod(Integer id){
        return paymentMethodRepository.existsById(id);
    }

    public PaymentMethodDto savePaymentMethod(PaymentMethodDto paymentMethodDto){
        PaymentMethod savePaymentMethod = toModel(paymentMethodDto);
        return toDto(paymentMethodRepository.save(savePaymentMethod));
    }

    public void deletePaymentMethod(Integer id){
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElse(null);
        assert paymentMethod != null;
        paymentMethodRepository.delete(paymentMethod);
    }

    public PaymentMethodDto updatePaymentMethodDto(PaymentMethodDto paymentMethodDto, Integer id){
        PaymentMethod updatePaymentMethod = paymentMethodRepository.findById(id).orElse(null);
        assert updatePaymentMethod != null;
        paymentMethodDto.setPaymentMethodId(id);
        updatePaymentMethod = toModel(paymentMethodDto);
        return toDto(paymentMethodRepository.save(updatePaymentMethod));
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
