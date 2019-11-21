package com.shop.manager.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import com.shop.manager.model.Product;
import com.shop.manager.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    
    public static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.POST,
                    consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, 
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity<?> uploadFile(@RequestParam(value = "files") MultipartFile[] files) {
        try {
            for (final MultipartFile file : files) {
                productService.saveProducts(file);
            }
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch(final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(method = RequestMethod.GET,
                    consumes = {MediaType.APPLICATION_JSON_VALUE},
                    produces = {MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllProduct() {
		return productService.getAllProducts().
                <ResponseEntity<?>>thenApply(ResponseEntity::ok).
                exceptionally(handleGetProductFailures);
    }

    private static Function<Throwable, ResponseEntity<? extends List<Product>>> handleGetProductFailures = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

}

