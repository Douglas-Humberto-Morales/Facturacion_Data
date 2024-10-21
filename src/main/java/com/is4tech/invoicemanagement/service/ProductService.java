package com.is4tech.invoicemanagement.service;

import com.is4tech.invoicemanagement.dto.ProductDto;
import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.model.Product;
import com.is4tech.invoicemanagement.repository.ProductRepository;
import com.is4tech.invoicemanagement.utils.Message;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<ProductDto> findByAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toDto);
    }

    public Message findAllProducts() {
        List<Product> listAllProducts = productRepository.findAll();

        Message message = new Message();

        message.setNote("All Products Retrieved Successfully");
        message.setObject(listAllProducts.stream().map(this::toDto).toList());

        return message;
    }

    public ProductDto findByIdProduct(Integer id) {
        return productRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public boolean existProduct(Integer id) {
        return productRepository.existsById(id);
    }

    public ProductDto save(ProductDto productDto) {
        Product saveProduct = toModel(productDto);
        return toDto(productRepository.save(saveProduct));
    }

    public void deleteProduct(Integer id) {
        Product deleteProduct = productRepository.findById(id).orElse(null);
        assert deleteProduct != null;
        productRepository.delete(deleteProduct);
    }

    public ProductDto updateProduct(ProductDto productDto, Integer id) {
        Product updateProduct = productRepository.findById(id).orElse(null);
        assert updateProduct != null;
        productDto.setProductsId(id);
        updateProduct = toModel(productDto);
        return toDto(productRepository.save(updateProduct));
    }

    private Product toModel(ProductDto productDto) {
        return Product.builder()
                .productID(productDto.getProductsId())
                .code(productDto.getCode())
                .name(productDto.getName())
                .deliveryTime(productDto.getDelivery_time())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .status((productDto.getStatus() != null) ? productDto.getStatus() : true)
                .companyOrBrandName(productDto.getCompanyOrBrandName())
                .expirationDate(productDto.getExpirationDate())
                .entryDate(productDto.getEntryDate())
                .stock(productDto.getStock())
                .build();
    }

    private ProductDto toDto(Product product) {
        return ProductDto.builder()
                .productsId(product.getProductID())
                .code(product.getCode())
                .name(product.getName())
                .delivery_time(product.getDeliveryTime())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .companyOrBrandName(product.getCompanyOrBrandName())
                .expirationDate(product.getExpirationDate())
                .entryDate(product.getEntryDate())
                .stock(product.getStock())
                .build();
    }
}
