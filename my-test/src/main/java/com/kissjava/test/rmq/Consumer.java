package com.kissjava.test.rmq;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;

public class Consumer {

	public static void main(String[] args) throws MQClientException {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(Constants.CONSUMER_ID);
		consumer.setNamesrvAddr(Constants.NAME_SRV);
		consumer.subscribe(Constants.TOPIC, Constants.TAG);
		
		
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
					ConsumeConcurrentlyContext context) {
				
				System.out.println(Thread.currentThread().getName()+ " Receive New Messages: " + msgs.size());

				MessageExt msg = msgs.get(0);
				if(msg.getTopic().equals("Topic")){
					System.out.println("content = " + new String(msg.getBody()));
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		consumer.createTopic("1001", Constants.TOPIC, 1);
		//consumer.start();
		System.out.println("consumer start..........");
		
	}

}
