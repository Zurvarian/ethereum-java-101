package ether.config;

import com.fasterxml.classmate.TypeResolver;
import io.vavr.collection.List;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.schema.AlternateTypeRules.newRule;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .alternateTypeRules(vavrCollectionsAsListRule())
                .select()
                .apis(basePackage("ether"))
                .paths(PathSelectors.any())
                .build();
    }

    private AlternateTypeRule vavrCollectionsAsListRule() {
        val typeResolver = new TypeResolver();

        return newRule(typeResolver.resolve(List.class, WildcardType.class),
                typeResolver.resolve(java.util.List.class, WildcardType.class));
    }
}
