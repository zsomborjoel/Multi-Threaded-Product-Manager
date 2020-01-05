package com.shop.manager.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.shop.manager.model.Product;
import com.shop.manager.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    /**
     * 
     * Saves data from csv file and sends back the Product list to the Controller
     */
    @Async
    public CompletableFuture<List<Product>> saveProducts(MultipartFile file) throws Exception {
        // Get transaction start time
        final long startTime = System.currentTimeMillis();

        List<Product> products = parseCsv(file);

        products = productRepository.saveAll(products);

        LOGGER.info("Transaction runtime was {} millisecounds", (System.currentTimeMillis() - startTime));
        return CompletableFuture.completedFuture(products);
    }

    /**
     * 
     * @param file csv - (productname,productprice,producttype)
     * @return List of products from requested csv file
     * @throws Exception
     */
    private List<Product> parseCsv(MultipartFile file) throws Exception {
        final List<Product> products = new ArrayList<Product>();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while((line = br.readLine()) != null) {
                final String[] productData = line.split(",");
                final Product product = new Product();
                product.setName(productData[0]);
                product.setPrice(Double.parseDouble(productData[1]));
                product.setType(productData[2]);
                products.add(product);
            }
            return products;
        } catch (Exception e) {
            LOGGER.error("Error occured during CSV file parsing {}", e);
            throw new Exception("Error occured during CSV file parsing {}", e);
        }
    }

    @Async
    public CompletableFuture<List<Product>> getAllProducts() {

        LOGGER.info("Request for all Products");

        final List<Product> products = productRepository.findAll();
        return CompletableFuture.completedFuture(products);
    }
}
