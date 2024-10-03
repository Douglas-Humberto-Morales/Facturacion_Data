package com.is4tech.invoicemanagement.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import com.is4tech.invoicemanagement.dto.DetailInvoiceProductsDto;
import com.is4tech.invoicemanagement.model.DetailInvoiceProducts;
import com.is4tech.invoicemanagement.repository.DetailInvoiceProductsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DetailInvoiceProductsService {
    private final DetailInvoiceProductsRepository detailInvoiceProductsRepository;

    public List<DetailInvoiceProductsDto> findByDetailInvoiceProduct(Pageable pageable){
        return detailInvoiceProductsRepository.findAll(pageable).stream()
            .map(this::toDto)
            .toList();
    }

    public DetailInvoiceProductsDto findByIdDetailInvoiceProducts(Integer id){
        return detailInvoiceProductsRepository.findById(id)
            .map(this::toDto)
            .orElseThrow(() -> new RuntimeException("Detail Invoice Products not found"));
    }

    public boolean existDetailInvoiceProducts(Integer id){
        return detailInvoiceProductsRepository.existsById(id);
    }

    public DetailInvoiceProductsDto saveDetailInvoiceProducts(DetailInvoiceProductsDto detailInvoiceProductsDto){
        DetailInvoiceProducts saveDetailInvoiceProducts = toModel(detailInvoiceProductsDto);
        return toDto(detailInvoiceProductsRepository.save(saveDetailInvoiceProducts));
    }

    public void deleteDetailInvoiceProduct(Integer id){
        DetailInvoiceProducts deleteDetailInvoiceProducts = detailInvoiceProductsRepository.findById(id).orElse(null);
        assert deleteDetailInvoiceProducts != null;
        detailInvoiceProductsRepository.delete(deleteDetailInvoiceProducts);
    }

    public DetailInvoiceProductsDto updateDetailInvoiceProducts(DetailInvoiceProductsDto detailInvoiceProductsDto, Integer id){
        DetailInvoiceProducts detailInvoiceProducts = detailInvoiceProductsRepository.findById(id).orElse(null);
        assert detailInvoiceProducts != null;
        detailInvoiceProductsDto.setDetailInvoiceProductsId(id);
        detailInvoiceProducts = toModel(detailInvoiceProductsDto);
        return toDto(detailInvoiceProductsRepository.save(detailInvoiceProducts));
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
