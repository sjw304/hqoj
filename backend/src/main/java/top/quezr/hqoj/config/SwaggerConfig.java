package top.quezr.hqoj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author que
 * @version 1.0
 * @date 2021/3/19 17:29
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {


    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalOperationParameters(globalOperationParameters());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Bread-API",
                "bread系统后端部分",
                "v0.0.1",
                "https://ssacgn.online/lyapi",
                new Contact("lzr", "https://ssacgn.online", "853681401@qq.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
    }

    private List<Parameter> globalOperationParameters() {
        return new ArrayList<>() {{
            add(new ParameterBuilder()
                    .name("Authorization")
                    .description("token")
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .required(false)
                    .build()
            );
        }};
    }
}
