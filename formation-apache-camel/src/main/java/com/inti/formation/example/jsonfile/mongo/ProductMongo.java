package com.inti.formation.example.jsonfile.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "product")
public class ProductMongo {
    @Id
    private Long id;
    private String libelle;
    private String origine;
    private String description;
    private List<String> couleur = new ArrayList<>();

}
