package com.amboucheba.soatp2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.amboucheba.soatp2"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "User Api Documentation",
                "This is the User api implemented for SOA's second Homework, it describes the crud operation of the user entity",
                "1.0",
                "/",
                new Contact("Amine Bouchebaba", "/", "bouchebaba.amine@gmail.com"),
                "/",
                "/",
                Collections.emptyList()
                );
    }
}
