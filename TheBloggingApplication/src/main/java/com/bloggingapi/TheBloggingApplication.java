package com.bloggingapi;

import java.util.Arrays;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TheBloggingApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("Hello World!!");
		ApplicationContext ctx = SpringApplication.run(TheBloggingApplication.class, args);
		
		System.out.println("List of Beans instantiated");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
        	if(beanName.equals("userController") || beanName.equals("categoryController")
        			|| beanName.equals("postController"))
        		System.out.println(beanName);
        }
	}

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TheBloggingApplication.class);
	}
}
