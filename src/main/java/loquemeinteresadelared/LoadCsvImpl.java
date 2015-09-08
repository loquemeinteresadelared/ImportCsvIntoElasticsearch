package loquemeinteresadelared;

import java.util.List;

import javax.annotation.Resource;

import loquemeinteresadelared.csv.CsvManager;
import loquemeinteresadelared.es.ESManager;

public class LoadCsvImpl implements LoadCsv {

	@Resource
	private CsvManager csvManagerImpl;
	@Resource
	private ESManager eSManagerImpl;
	
	@Override
	public void process() throws Exception {
		List<String> headers = csvManagerImpl.headers();
		List<String> columns = null;
		while ((columns = csvManagerImpl.line()) != null) {
			eSManagerImpl.addBulk(headers, columns);
		}
	}

}
