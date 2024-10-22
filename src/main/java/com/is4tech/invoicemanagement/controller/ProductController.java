package com.is4tech.invoicemanagement.controller;

import com.is4tech.invoicemanagement.dto.CustomerDto;
import com.is4tech.invoicemanagement.dto.ProductDto;
import com.is4tech.invoicemanagement.exception.BadRequestException;
import com.is4tech.invoicemanagement.exception.ResourceNorFoundException;
import com.is4tech.invoicemanagement.model.Customer;
import com.is4tech.invoicemanagement.model.Product;
import com.is4tech.invoicemanagement.service.ProductService;
import com.is4tech.invoicemanagement.utils.Message;
import com.is4tech.invoicemanagement.utils.MessagePage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice-management/v0.1/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private static final String NAME_ENTITY = "Product";
    private static final String ID_ENTITY = "products_id";

    @GetMapping("/show-all")
    public ResponseEntity<MessagePage> showAllProducts(Pageable pageable) {
        Page<ProductDto> productPage = productService.findByAllProducts(pageable);

        if (productPage.isEmpty()) {
            throw new ResourceNorFoundException("No products found");
        }

        MessagePage message = MessagePage.builder()
                .note("Records Found")
                .object(productPage.getContent())
                .totalElements((int) productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .currentPage(productPage.getNumber())
                .pageSize(productPage.getSize())
                .build();

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/show-all-products")
    public ResponseEntity<Message> showAllProducts() {
        Message listProducts = productService.findAllProducts();

        return new ResponseEntity<>(
                Message.builder()
                        .note("Records found")
                        .object(listProducts.getObject())
                        .build(),
                HttpStatus.OK
        );
    }

    @GetMapping("/show-by-id/{id}")
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

    @PostMapping("/create")
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

    @PutMapping("/update/{id}")
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

    @DeleteMapping("/delete/{id}")
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

    @PostMapping("/search-by-name")
    public ResponseEntity<MessagePage> searchProductByName(
            @RequestBody ProductDto productDto,
            HttpServletRequest request, Pageable pageable) {

        Page<ProductDto> productPage = productService.findByName(productDto.getName(), pageable);

        return new ResponseEntity<>(MessagePage.builder()
                .note("Records found")
                .object(productPage.getContent())
                .totalElements((int) productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .currentPage(productPage.getNumber())
                .pageSize(productPage.getSize())
                .build(),
                HttpStatus.OK);
    }

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<Product> toggleProductStatus(@PathVariable Integer id, HttpServletRequest request) {
        try {
            Product updatedProduct = productService.toggleProductStatus(id);

            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (ResourceNorFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new com.is4tech.invoicemanagement.exception.BadRequestException("Error" + e.getMessage());
        }
    }
}
