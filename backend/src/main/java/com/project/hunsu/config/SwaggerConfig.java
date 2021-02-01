package com.project.hunsu.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.project.hunsu.controller"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo swaggerInfo(){
        return new ApiInfoBuilder()
                .title("Hunsu API Documnentation")
                .description(
                        "Hunsu Resource API 문서입니다!\n\n백엔드 담당자\n OOTD : 고유창\n 뭘입을까 : 박홍균\n 인증 : 정보현\n"+
                        "**********\n작업 완료(O)\n작업 중(~)\n작업 전(X)\n**********")
                .version("0.2")
                .build();
    }
}

