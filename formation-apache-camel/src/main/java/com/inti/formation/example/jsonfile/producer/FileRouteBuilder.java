package com.inti.formation.example.jsonfile.producer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inti.formation.example.jsonfile.businessterm.product.ProductInput;
import com.inti.formation.example.jsonfile.mongo.ServiceProduct;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.inti.formation.example.jsonfile.converter.Converter;

import java.util.UUID;

/**
 * 
 * @author Sylvanius Kouandongui
 *
 */
@Component
@Slf4j
public class FileRouteBuilder extends RouteBuilder {

	@Autowired
	private Converter converter;

	@Autowired
	ServiceProduct serviceProduct;

	/**
	 * Uses of apache camel to save event to mongo
	 * 
	 * @throws Exception
	 */
	@Override

	public void configure() throws Exception {
		// 	from("sftp://{{in.sftp.user}}@{{in.sftp.host}}:{{in.sftp.port}}/{{in.directory}}?password={{in.sftp.password}}&include={{in.file.filemask}}&preMove=.tmp/&move=.done/${date:now:yyyyMMdd}/${file:name}&sortBy=file:modified&moveFailed=.error/${date:now:yyyyMMdd}/${file:name}")
		from("file://{{in.directory}}?include={{in.file.filemask}}&preMove=.tmp/&move=.done/${date:now:yyyyMMdd}/${file:name}&sortBy=file:modified&moveFailed=.error/${date:now:yyyyMMdd}/${file:name}")
				.routeId("process_file")
				.setHeader("uid")
				.constant(UUID.randomUUID().toString())
				. process(exchange -> {
					log.info("FileUid received "+  exchange.getIn().getHeader("uid", String.class) + " from file " + exchange.getIn().getHeader("CamelFileName") + " will be pushed in kafka topic" );
				}).process(exchange -> {
					final String fileName = exchange.getIn().getHeader("CamelFileNameOnly", String.class);
				}).
				from("direct:init_file")
				.routeId("init_file")
				.removeHeaders("CamelFile*", "CamelFileNameOnly")
				.split()
				.jsonpath("$[*]")
				.streaming()
				.to("direct:process_file_processing");

			   from("direct:process_file_processing")
				.routeId("process_file_to_mongo")
				// unmarshal to convert json to POJO example:
				//.unmarshal()
				// .json(JsonLibrary.Jackson, Product.class)
					.marshal()
					.json(JsonLibrary.Jackson)
					.convertBodyTo(String.class)
					.process(exchange -> {
						final String  body = exchange.getIn().getBody(String.class);
						ProductInput product = new ObjectMapper().readValue(body, ProductInput.class);
						serviceProduct.register( converter.convert(product));
						log.info("   product " +product.toString());

	  					}
	  			).end();
				
	}
	



}
