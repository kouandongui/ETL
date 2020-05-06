package com.inti.formation.example.jsonfile.mongo;

import org.springframework.stereotype.Component;

@Component
public interface ServiceProduct {
    ProductMongo register(ProductMongo p);
}
