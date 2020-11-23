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
import java.util.function.Predicate;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.amboucheba.soatp2"))
                .paths(PathSelectors.any())
                .paths(Predicate.not(PathSelectors.ant("/documentation")) )
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfo(
                "User Api Documentation",
                "This is the User api implemented for SOA's second Homework, it describes the crud operation of the user entity",
                "1.0",
                null,
                new Contact("Amine Bouchebaba", null, "bouchebaba.amine@gmail.com"),
                null,
                null,
                Collections.emptyList()
                );
    }
}
