package com.crt.openapi.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crt.openapi.storm.bolts.GetCategoryBolt;
import com.crt.openapi.storm.bolts.NewsNotifierBolt;
import com.crt.openapi.storm.bolts.ProductCategoriesCounterBolt;
import com.crt.openapi.storm.bolts.UserHistoryBolt;
import com.crt.openapi.storm.spouts.UsersNavigationSpout;

public class TopologyStarter {
	
	private static final Logger msglog = LoggerFactory.getLogger(TopologyStarter.class);
	
	private static String REDIS_HOST = "";
	private static String REDIS_PORT = "";
	private static String WEBSERVER = "";
	
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("read-feed", new UsersNavigationSpout(), 3);
		
		builder.setBolt("get-categ", new GetCategoryBolt(), 3)
			   .shuffleGrouping("read-feed");
		
		builder.setBolt("user-history", new UserHistoryBolt(), 5)
			   .fieldsGrouping("get-categ", new Fields("user"));
		
		builder.setBolt("product-counter", new ProductCategoriesCounterBolt(), 5)
			   .fieldsGrouping("user-history", new Fields("product"));
		
		builder.setBolt("news-notifier", new NewsNotifierBolt(), 5)
		       .shuffleGrouping("product-counter");
		
		Config config = new Config();
		config.setDebug(true);
		config.put("redis-host", REDIS_HOST);
		config.put("redis-port", REDIS_PORT);
		config.put("webserver", WEBSERVER);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("analytics", config, builder.createTopology());
	}

}
