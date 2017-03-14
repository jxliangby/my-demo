package com.kissjava.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@SpringBootApplication
public class BookstoreApplication extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer{
	 @Override  
	 public void customize(ConfigurableEmbeddedServletContainer container) {  
	      container.setPort(8090);  
	 }  
	 @RequestMapping(value = "/recommended")
	 public String readingList(){
	    return "Spring in Action (Manning), Cloud Native Java (O'Reilly), Learning Spring Boot (Packt)";
	 }

	 public static void main(String[] args) {
	    SpringApplication.run(BookstoreApplication.class, args);
	 }
}
