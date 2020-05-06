package com.inti.formation.example.jsonfile.converter;


import com.inti.formation.example.jsonfile.businessterm.product.ProductInput;
import com.inti.formation.example.jsonfile.mongo.ProductMongo;
import org.springframework.stereotype.Component;

/**
 * @author Sylvanius KOuandongui
 *
 */
@Component
public class ConverterImpl implements Converter {
    @Override
    public ProductMongo convert(ProductInput productInput) {
        ProductMongo productMongo = new ProductMongo();
        productMongo.setId(productInput.getId() );
        return productMongo;
    }
}
