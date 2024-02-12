package kg.kadyrbekov.config;

import com.fasterxml.classmate.TypeResolver;
import kg.kadyrbekov.dto.UserResponse;
import kg.kadyrbekov.error.ApiError;
import kg.kadyrbekov.mapper.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private final TypeResolver typeResolver;

    @Autowired
    public SwaggerConfig(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .additionalModels(typeResolver.resolve(ApiError.class))
                .additionalModels(typeResolver.resolve(LoginResponse.class))
                .additionalModels(typeResolver.resolve(UserResponse.class))
                .select()
                .apis(RequestHandlerSelectors.basePackage("kg.kadyrbekov.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GameS API")
                .description("Spring Rest API reference")
                .licenseUrl("kadyrbekovtimur001@gmail.com")
                .version("1.0")
                .build();
    }

}