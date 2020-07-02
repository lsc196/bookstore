package com.lsc.bootstore.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    static final Logger log = LoggerFactory.getLogger(SwaggerConfig.class);

    @Value("${swagger.enable}")
    boolean enableSwagger = true;

    @Bean
    public Docket createRestApi() {
        log.info("开始加载Swagger2...");
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger)
                .apiInfo(apiInfo()).select()
                //扫描指定包中的swagger注解
                .apis(RequestHandlerSelectors.basePackage("com.lsc.bootstore.controller"))
                //扫描所有有注解的api，用这种方式更灵活
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("网上书城接口文档")
                .description("网上书城各种接口信息")
                .version("2.0.0")
                .build();
    }
}
