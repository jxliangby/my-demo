package com.kissjava.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//
//import com.netflix.hystrix.HystrixCommand;
//import com.netflix.hystrix.HystrixCommandGroupKey;
//import com.netflix.hystrix.HystrixCommandProperties;

//@RestController
//@SpringBootApplication
public class ReadingApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {
	@Autowired
	private BookService bookService;

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.setPort(8091);
	}

	@RequestMapping("/to-read")
	public String readingList() {
		return "null";//new ReadCommand("read").execute();
	}
//	class ReadCommand extends HystrixCommand<String>{
//		private String name;
//		public ReadCommand(String name) {
//			super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("helloFallbak"))
//					.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
//							.withExecutionTimeoutInMilliseconds(500)));
//			this.name = name;
//		}
//		
//		@Override
//		protected String getFallback(){
//			return bookService.reliable();
//		}
//		
//		@Override
//		protected String run() throws Exception {  
//			return bookService.readingList() ; 
//		}
//	}
	public static void main(String[] args) {
		SpringApplication.run(ReadingApplication.class, args);
	}
}