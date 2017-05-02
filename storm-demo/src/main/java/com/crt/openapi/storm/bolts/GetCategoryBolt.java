package com.crt.openapi.storm.bolts;

import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import com.crt.openapi.storm.entity.NavigationEntry;
import com.crt.openapi.storm.entity.Product;
import com.crt.openapi.storm.entity.ProductsReader;

public class GetCategoryBolt  extends BaseBasicBolt{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6057283346186525042L;
	
	private ProductsReader reader;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		String host = stormConf.get("redis-host").toString();
		int port = Integer.valueOf(stormConf.get("redis-port").toString());

		this.reader = new ProductsReader(host, port); 
		super.prepare(stormConf, context);
	}
	
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		NavigationEntry entry = (NavigationEntry)input.getValue(1);
		if("PRODUCT".equals(entry.getPageType())){
			try {
				String product = (String)entry.getOtherData().get("product");

				Product itm = reader.readItem(product);
				if(itm ==null)
					return ;

				String categ = itm.getCategory();

				collector.emit(new Values(entry.getUserId(), product, categ));

			} catch (Exception ex) {
				System.err.println("Error processing PRODUCT tuple"+ ex);
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("user","product", "categ"));
	}

}
