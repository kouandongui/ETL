package com.inti.formation.example.jsonfile.businessterm.product;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data

public class ProductInput {
    private Long id;
    private String libelle;
    private String origine;
    private String description;
    private List<String> couleur = new ArrayList<>();
}
