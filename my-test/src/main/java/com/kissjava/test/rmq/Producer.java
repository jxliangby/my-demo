package com.kissjava.test.rmq;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class Producer {
	
	//public static final String PRODUCER_NAME = "PRODUCER_NAME";
	
	public static String nowStr(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-mm-HHMMSS");
		return sdf.format(new Date());
	}
	
	
	public static void main(String[] args) throws MQClientException {
		DefaultMQProducer producer = new DefaultMQProducer(Constants.PRODUCER_ID);
		producer.setNamesrvAddr(Constants.NAME_SRV);
		producer.setInstanceName("producer");
		producer.start();
		
		for(int index=0 ,size=10; index<size; index++){
			Message message = new Message(Constants.TOPIC, Constants.TAG, "order_"+index, nowStr().getBytes());
			try {
				producer.send(message);
				Thread.sleep(5000);
			} catch (RemotingException e) {
				e.printStackTrace();
			} catch (MQBrokerException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
