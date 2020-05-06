package com.inti.formation.example.jsonfile.converter;



import com.inti.formation.example.jsonfile.businessterm.product.ProductInput;
import com.inti.formation.example.jsonfile.mongo.ProductMongo;
import org.apache.camel.Exchange;

/**
 * @author Sylvanius Kouandongui
 *
 */
public interface Converter {


	ProductMongo convert(final ProductInput p);
}
