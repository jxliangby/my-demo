package com.crt.openapi.storm.spouts;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.crt.openapi.storm.entity.NavigationEntry;

import redis.clients.jedis.Jedis;

public class UsersNavigationSpout extends BaseRichSpout{
	private static final Logger msglog = LoggerFactory.getLogger("UsersNavigationSpout");
	
	private Jedis jedis;
	private String host;
	private int port;
	private SpoutOutputCollector collector;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8570315830954225792L;

	@SuppressWarnings("rawtypes")
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		host = conf.get("redis-host").toString();
		port = Integer.parseInt(conf.get("redis-port").toString());
		this.collector = collector;
		reconnect();
	}

	private void reconnect(){
		jedis = new Jedis(host, port);
	}
	
	@Override
	public void nextTuple() {
		String content = jedis.rpop("navigation");
		if(content == null || "nil".equals(content)){
			try { 
				Thread.sleep(300); 
			} catch (InterruptedException e) {
				
			}
			return;
		}
		
		JSONObject json = JSONObject.parseObject(content);
		String user = json.getString("user");
		String product = json.getString("product");
		String type = json.getString("type");
		
		Map<String, String> map = new HashMap<>();
		map.put("product", product);
		NavigationEntry entry = new NavigationEntry(user, type, map);
		collector.emit(new Values(user,entry));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		 declarer.declare(new Fields("user", "otherdata"));
	}

}
