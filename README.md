# Importar ficheros CSV en Elasticsearch

Este proyecto se ha creado para el artículo [elasticsearch, importar ficheros CSV utilizando el API Bulk de Java](https://loquemeinteresadelared.wordpress.com/2015/09/08/elasticsearch-importar-ficheros-csv-utilizando-el-api-bulk-de-java/).


## Descripción

El principal objetivo de este proyecto es el de mostrar el uso del API Bulk de elasticsearch. Para ello hemos creado un proyecto que carga un fichero CSV cualquiera en elasticsearch. La creación del mapping se deja que sea elasticsearch el se encargue de su creación, aunque como siempre es recomentable que lo hagamos nosotros. 


## Formato del fichero CSV  

**Primera fila**. Nombre de las columnas separadas por el carácter pipe |. Este nombre de las columnas será también el utilizado por el documento JSON que irá a elasticsearch.

**Resto de filas**. Valores de las columnas separados por el caráter pipe |.

Un fichero de ejemplo se encuentra en este mismo proyecto, es el fichero **example.csv**. La estructura es:

~~~~
Orden|Apellido|Total
1|GARCIA|1476378
2|GONZALEZ|929938
3|RODRIGUEZ|928305
~~~~

## Formato del tipo de documento de elasticsearch

Su estructura depende el fichero CSV. Así por ejemplo, para el fichero CSV incluido en este proyecto estos son documentos Json válidos para elasticsearch.

~~~~
{"orden": "1", "apellido": "GARCIA", "total": "1476378" }
{"orden": "2", "apellido": "GONZALEZ", "total": "929938" }
{"orden": "3", "apellido": "RODRIGUEZ", "total": "928305" }
~~~~

## Versión de frameworks y software utilizado

- Spring 4.2.1.RELEASE
- Log4j 1.2.14
- Elasticsearch Java API client 1.7.1
- Java 8

## Clases Principales y ficheros de configuración

- loquemeinteresadelared.conf.AppConfig. Clase de configuración basada en anotaciones de Spring.
- loquemeinteresadelared.MainProcess. Clase *main* que inicia la carga del fichero csv.
- loquemeinteresadelared.LoadCsvImpl. Clase principal que organiza la carga del fichero CSV en elasticsearch.
- loquemeinteresadelared.csv.CsvManagerImpl. Lee el fichero CSV línea a línea.
- loquemeinteresadelared.es.ESManagerImpl. Se conecta a elasticsearch utilizando *Transport client* y prepara la carga bulk.
- default.properties y development.properties. Son ficheros de configuración para un entorno local y un entorno de desarrollo donde se guarda la configuración de acceso a elasticsearch, la ruta fichero csv, la configuración del bulk etc. Sus valores pueden ser sobreescritos por propiedades de la Jvm en el arranque de la aplicación.


## Ejecución 

**Arranque por defecto, entorno local**. Podemos indicar la propiedad spring.profiles.active o dejarla sin indicar. 

~~~~
loquemeinteresadelared.MainProcess 
~~~~

~~~~

loquemeinteresadelared.MainProcess -Dspring.profiles.active=default
~~~~

**Arranque entorno development**

loquemeinteresadelared.MainProcess -Dspring.profiles.active=development