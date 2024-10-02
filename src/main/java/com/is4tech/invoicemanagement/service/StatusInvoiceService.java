package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.is4tech.invoicemanagement.dto.StatusInvoiceDto;
import com.is4tech.invoicemanagement.model.StatusInvoice;
import com.is4tech.invoicemanagement.repository.StatusInvoiceRepository;

import java.util.List;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StatusInvoiceService {
    private final StatusInvoiceRepository statusInvoiceRepository;

    public List<StatusInvoiceDto> listAllStatusInvoice(Pageable pageable){
        return statusInvoiceRepository.findAll(pageable).stream()
            .map(this::toDto)
            .toList();
    }

    public StatusInvoiceDto findByIdStatusInvoice(Integer id){
        return statusInvoiceRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Status Invoice not found"));
    }

    public Boolean existStatusInvoice(Integer id){
        return statusInvoiceRepository.existsById(id);
    }

    public StatusInvoiceDto saveStatusInvoice(StatusInvoiceDto statusInvoicedDto){
        StatusInvoice saveStatusInvoice = toModel(statusInvoicedDto);
        return toDto(statusInvoiceRepository.save(saveStatusInvoice));
    }

    public void deleteStatusInvoice(Integer id){
        StatusInvoice deleteStatusInvoice = statusInvoiceRepository.findById(id).orElse(null);
        assert deleteStatusInvoice != null;
        statusInvoiceRepository.delete(deleteStatusInvoice);
    }

    public StatusInvoiceDto updateStatusInvoice(StatusInvoiceDto statusInvoideDto, Integer id){
        StatusInvoice updateStatusInvoice = statusInvoiceRepository.findById(id).orElse(null);
        assert updateStatusInvoice != null;
        updateStatusInvoice.setName(statusInvoideDto.getName());
        return toDto(statusInvoiceRepository.save(updateStatusInvoice));
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
