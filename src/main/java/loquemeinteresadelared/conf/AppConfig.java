package loquemeinteresadelared.conf;

import loquemeinteresadelared.LoadCsv;
import loquemeinteresadelared.LoadCsvImpl;
import loquemeinteresadelared.csv.CsvManager;
import loquemeinteresadelared.csv.CsvManagerImpl;
import loquemeinteresadelared.es.ESManager;
import loquemeinteresadelared.es.ESManagerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/${spring.profiles.active:default}.properties")
public class AppConfig {

	@Autowired
    Environment env;
	
	@Bean public LoadCsv loadCsvImpl() throws Exception {
		LoadCsvImpl load = new LoadCsvImpl();
		return load;
	}
	
	@Bean public CsvManager csvManagerImpl() throws Exception {
		return new CsvManagerImpl(env.getProperty("csv.path"), env.getProperty("csv.char.separator"), env.getProperty("csv.charset"));
	}
	
	@Bean public ESManager eSManagerImple() {
		return new ESManagerImpl()
			.setBulkConcurrentRequests(Integer.valueOf(env.getProperty("elasticsearch.bulk.concurrent.requests")))
			.setBulkSize(Integer.valueOf(env.getProperty("elasticsearch.bulk.size")))
			.setElasticsearchNombreCluster(env.getProperty("elasticsearch.nombre.cluster"))
			.setElasticsearcHost(env.getProperty("elasticsearch.host"))
			.setIndice(env.getProperty("elasticsearch.indice"))
			.setTipoDocumento(env.getProperty("elasticsearch.tipo.documento"));
	}
}
