package com.inti.formation.example.jsonfile.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


    @Repository
    public interface ProductRepository extends MongoRepository<ProductMongo, Long> {
}
