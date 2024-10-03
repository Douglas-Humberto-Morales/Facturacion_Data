package com.is4tech.invoicemanagement.controller;

import com.is4tech.invoicemanagement.dto.ProductDto;
import com.is4tech.invoicemanagement.exception.BadRequestException;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.service.CustomerService;
import com.is4tech.invoicemanagement.service.ProductService;
import com.is4tech.invoicemanagement.utils.Message;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice-management/v0.1/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService, CustomerService customerService) {
        this.productService = productService;
    }

    private static final String NAME_ENTITY = "Product";
    private static final String ID_ENTITY = "products_id";

    @GetMapping("/products")
    public ResponseEntity<Message> showAllProducts(Pageable pageable) {
        List<ProductDto> listProducts = productService.findByAllProducts(pageable);
        if (listProducts.isEmpty())
            throw new ResourceNorFoundException(NAME_ENTITY);

        return new ResponseEntity<>(Message.builder()
                .note("Records Found")
                .object(listProducts)
                .build(),
                HttpStatus.OK);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Message> showProductById(@PathVariable Integer id) {
        ProductDto productDto = productService.findByIdProduct(id);

        if (productDto == null)
            throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());

        return new ResponseEntity<>(Message.builder()
                .note("Record Found")
                .object(productDto)
                .build(),
                HttpStatus.OK);

    }

    @PostMapping("/product")
    public ResponseEntity<Message> saveProduct(@RequestBody ProductDto productDto) throws BadRequestException {
        try {
            ProductDto saveProduct = productService.save(productDto);
            return new ResponseEntity<>(Message.builder()
                    .note("Saved Successfully")
                    .object(saveProduct)
                    .build(),
                    HttpStatus.CREATED);
        } catch (DataAccessException e) {
            throw new BadRequestException("Error save record: " + e.getMessage());
        }
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<Message> updateProduct(@PathVariable Integer id, @RequestBody @Valid ProductDto productDto) throws BadRequestException {
        try {
            if (productService.existProduct(id)) {
                ProductDto updateProduct = productService.updateProduct(productDto, id);
                return new ResponseEntity<>(Message.builder()
                        .note("Updated Successfully")
                        .object(updateProduct)
                        .build(),
                        HttpStatus.OK);
            } else
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
        } catch (DataAccessException e) {
            throw new BadRequestException("Error update record: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<Message> deleteProduct(@PathVariable Integer id) throws BadRequestException {
        try {
            if (productService.existProduct(id)) {
                productService.deleteProduct(id);
                return new ResponseEntity<>(Message.builder()
                .note("Deleted Successfully")
                        .object(null)
                        .build(),
                        HttpStatus.OK);
            } else {
                throw new ResourceNorFoundException(NAME_ENTITY, ID_ENTITY, id.toString());
            }
        } catch (DataAccessException e) {
            throw new BadRequestException("Error delete record: " + e.getMessage());
        }
    }
}
