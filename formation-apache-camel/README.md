## ETL APACHE CAMEL 
* https://camel.apache.org/

## Parsing JSON 

```bash
DATA
[{
  "id": 1,
  "libelle": "crayon",
  "origine": "Espagne",
  "description": "crayon d'origine Espagne",

  "couleur": ["jaune", "gris", "bleu"]

},
  {
    "id": 2,
    "libelle": "crayon",
    "origine": "Espagne",
    "description": "crayon d'origine Espagne",
    "couleur": ["jaune", "gris", "bleu"]

  }
]

PARSING 

.split()
.jsonpath("$[*]")
.streaming()
```

```bash
DATA 
   {
      "file-uid":"d8a31800-5f36-11e8-8de0-c9a1e709a425",
      "contents":[
         {
            "request":{
               "type":"PUT",
               "rdo":"tableValeur"
            },
            "data":{
               "tableOfValuesTable":"LISTE_TABLE_8",
               "tableOfValuesList":[
                  {
                     "tableOfValuesCode":"TATA",
                     "tableOfValuesDefinition":"Liste des ThÉo"
                  }
               ]
            }
         },
         {
            "request":{
               "type":"PUT",
               "rdo":"tableValeur"
            },
            "data":{
               "tableOfValuesTable":"LISTE_TABLE_9",
               "tableOfValuesList":[
                  {
                     "tableOfValuesCode":"TOTO",
                     "tableOfValuesDefinition":"Liste des ThEo"
                  }
               ]
            }
         },
          {
                "request": {
                    "type": "DELETE",
					"rdo":"tableValeur"
                },
                "metadata": {
                    "technical_ID": "LISTE_TABLE_7"
                }
            }
      ]
   }

PARSING 

.jsonpath("$.file-uid", String.class)
			.setHeader("requestType")
				.jsonpath("$.contents.[*].request.type", String.class)


DATA 
{"id":"1","internalId":"1","name":"LMPL PV National","type":"NATZ"}
{"id":"2","internalId":"2","name":"LMPL Pologne","type":"REGZ"}
{"id":"3","internalId":"3","name":"LMPL Web","type":"WEBZ"}
{"id":"4","internalId":"4","name":"LMPL Franchised Zone","type":"VIRZ"}
{"id":"5","internalId":"5","name":"LMPL B2B Zones","type":"B2BZ"}

.split(body().tokenize("\n"))
				.streaming()

TRANSFORMATION
				.unmarshal() // json en POJO ( Classe) 
				.json(JsonLibrary.Jackson, Price.class)

```
XML 

```bash

<ROOT>
  <ENSEIGNE_ID>SHOP</ENSEIGNE_ID>
  <MAG>524</MAG>
  <DATE>20170921003011</DATE>
</ROOT>

PARSING 

.convertBodyTo(String.class)
.setHeader("site", xpath("/ROOT/MAG/text()", Integer.class))
.setHeader("date", xpath("/ROOT/DATE/text()", String.class))

```
