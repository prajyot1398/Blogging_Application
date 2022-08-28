package com.bloggingapi.swagger;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig implements WebMvcConfigurer {
    
    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    private List<SecurityContext> securityContexts() {
        return List.of(SecurityContext.builder()
                .securityReferences(securityReferences())
                .build());
    }

    private List<SecurityReference> securityReferences() {

        AuthorizationScope scope = new AuthorizationScope("global", "Access Everything");
        return List.of(new SecurityReference("JWT", new AuthorizationScope[] {scope}));
    }

    @Bean
    public Docket apiDocket() {
        
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(getInfo())
            .securityContexts(securityContexts())
            .securitySchemes(List.of(apiKey()))
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.bloggingapi"))
            .paths(PathSelectors.any())
            .build()
        ;
    }

    private ApiInfo getInfo() {

        return new ApiInfo(
            "The Blogging Application"
            ,"This is REST API for The Blogging Application Developed By Prajyot Jadhav With Love. User Can Create Blog Posts With Different Categories"
            +" And Other Users Can Also Comment On The Blog Post From Different Users. It Supports Role Based Authorization"
            +" And JWT based Authentication."
            , "1.0"
            , null
            , new Contact("Prajyot Jadhav", null, "prajyot13.98@gmail.com")
            , null
            , null
            , Collections.emptyList());
    }

    //For Static resources
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
        .addResourceLocations("classpath:/static/")
        .setCacheControl(CacheControl.maxAge(2, TimeUnit.HOURS).cachePublic());
	}
}
