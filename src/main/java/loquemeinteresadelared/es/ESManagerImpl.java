package loquemeinteresadelared.es;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;


public class ESManagerImpl implements ESManager {

	public static final Log log = LogFactory.getLog(ESManagerImpl.class);

	private String elasticsearcHost;
	private String elasticsearchNombreCluster;
	private String indice;
	private String tipoDocumento;
	private Integer bulkSize;
	private Integer bulkConcurrentRequests;
	private BulkProcessor bulk;
	private Client client;
	
	public ESManagerImpl setElasticsearcHost(String elasticsearcHost) {
		this.elasticsearcHost = elasticsearcHost;
		return this;
	}
	public ESManagerImpl setElasticsearchNombreCluster(String elasticsearchNombreCluster) {
		this.elasticsearchNombreCluster = elasticsearchNombreCluster;
		return this;
	}
	public ESManagerImpl setIndice(String indice) {
		this.indice = indice;
		return this;
	}
	public ESManagerImpl setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
		return this;
	}
	public ESManagerImpl setBulkSize(Integer bulkSize) {
		this.bulkSize = bulkSize;
		return this;
	}
	public ESManagerImpl setBulkConcurrentRequests(Integer bulkConcurrentRequests) {
		this.bulkConcurrentRequests = bulkConcurrentRequests;
		return this;
	}

	//public ESManagerImpl(String elasticsearcHost, String elasticsearchNombreCluster, String indice, String tipoDocumento) {
	public ESManagerImpl() {}
	
	@SuppressWarnings("resource")
	@PostConstruct
	private void init() throws Exception {
		Settings settings = ImmutableSettings.settingsBuilder()
			.put("cluster.name", elasticsearchNombreCluster)
			.put("client.transport.sniff", true)
			.build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(elasticsearcHost, 9300));
		bulk = prepareBulk();
	}
	
	@PreDestroy
	public void shutdown() {
		try {
			bulk.awaitClose(15, TimeUnit.MINUTES);
		} catch (InterruptedException e) {e.printStackTrace();}
		client.close();
	}
	
	@Override
	public void addBulk(List<String> headers, List<String> values) throws Exception {
		XContentBuilder json = jsonBuilder().startObject();
		for (int i=0;i<headers.size();i++) {
			String header = headers.get(i).toLowerCase();
			String value = values.get(i);
			json.field(header, value);
		}
		bulk.add(new IndexRequest(this.indice, this.tipoDocumento).source(json));
	}

	private BulkProcessor prepareBulk() throws Exception {
		return bulk = BulkProcessor.builder(this.client, new BulkProcessor.Listener() {
			@Override
			public void beforeBulk(long id, BulkRequest bulk) {
				log.info(String.format("Bulk %d preparado para ser lanzado con un size de %d bytes", id, bulk.estimatedSizeInBytes()));
			}			
			@Override
			public void afterBulk(long id, BulkRequest bulk, Throwable exc) {
				log.error(String.format("Bulk %d procesado con error: %s", id, exc.getMessage() ));
			}
			@Override
			public void afterBulk(long id, BulkRequest bulkReq, BulkResponse bulkResp) {
				log.info(String.format("Bulk %d procesado, en %d milisegundos", id, bulkResp.getTookInMillis() ));
				if (bulkResp.hasFailures()) {
					log.error(String.format("Bulk %d procesado con fallos en %d milisegundos", id, bulkResp.getTookInMillis() ));
					log.error(bulkResp.buildFailureMessage());
				}
			}
		})
		.setBulkActions(this.bulkSize)
		.setConcurrentRequests(this.bulkConcurrentRequests)
		.build();
	}	

}