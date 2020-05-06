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
		//from("sftp://{{in.sftp.user}}@{{in.sftp.host}}:{{in.sftp.port}}/{{in.directory}}?password={{in.sftp.password}}&include={{in.file.filemask}}&preMove=.tmp/&move=.done/${date:now:yyyyMMdd}/${file:name}&sortBy=file:modified&moveFailed=.error/${date:now:yyyyMMdd}/${file:name}")
		from("file://{{in.directory}}" + // repertoire
				"?include={{in.file.filemask}}" + // extension .json ou .JSON
				"&preMove=.tmp/" + // Traitement se fait dans le dossier temporaire de nom .tmp
				"&move=.done/" + // Quand le traitment est un succès dépose le fichier ke dossier .done
				"${date:now:yyyyMMdd}/" + // création d'un sous dossier de .done par date
				"${file:name}&sortBy=file:modified" + // recopier le fichier et les trier par nom
				"&moveFailed=.error/" + // créer un sous .error quand un problème sur le fichier
				"${date:now:yyyyMMdd}/" + // // création d'un sous dossier de .error par date
				"${file:name}") // copier le fichier
				.routeId("process_file") // identifiant  du point de départ
				.setHeader("uid") // identifiant de la donnée  pour la tyraçabilité
				.constant(UUID.randomUUID().toString()) // uid=   UUID.randomUUID().toString()
				.process(exchange -> {
					// exchange.getIn().getHeader("CamelFileName")  CamelFileName = nom du fichier
					log.info("FileUid received "+  exchange.getIn().getHeader("uid", String.class) + " from file " + exchange.getIn().getHeader("CamelFileName") + " will be pushed in mongodb" );
				}).process(exchange -> {
					final String fileName = exchange.getIn().getHeader("CamelFileNameOnly", String.class);
				}).
				from("direct:init_file")
				.routeId("init_file")
				.removeHeaders("CamelFile*", "CamelFileNameOnly")

				.split( ) // comment je lis le fichier
				.jsonpath("$[*]") // parser json
				.streaming()  // pas de chargement en  mémoire
				.to("direct:process_file_processing");

				// stockage dans mongo
			   from("direct:process_file_processing")
				.routeId("process_file_to_mongo")
				      // unmarshal to convert json to POJO example:
					   // .unmarshal()
				   	// .json(JsonLibrary.Jackson, Product.class)

					.marshal() // Conversion de en json
					.json(JsonLibrary.Jackson)
					.convertBodyTo(String.class) // convertit je json  en string
					.process(exchange -> {
						// Récupération du produit en string
						final String  body = exchange.getIn().getBody(String.class);
						// conversion du string en object ProductInPut
						ProductInput product = new ObjectMapper().readValue(body, ProductInput.class);
						// Sauvegarde dans mongo
						serviceProduct.register( converter.convert(product));
						log.info("   product " +product.toString());

	  					}
	  			).end();
				
	}
	



}
