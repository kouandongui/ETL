package com.inti.formation.example.jsonfile.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceProductImpl implements ServiceProduct {
    @Autowired
    ProductRepository productRepository;
    @Override
    public ProductMongo register(ProductMongo p) {
        return productRepository.save( p);
    }
}
