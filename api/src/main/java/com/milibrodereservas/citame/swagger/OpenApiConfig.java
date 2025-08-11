package com.milibrodereservas.citame.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Comparator;
import java.util.List;

/* Clase de configuracion para Swagger */

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("citame")
                        .version("v1")
                        .description("API citame app"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ))
                // quitado para no aplicar el esquema de seguridad de manera global
                // (que aparezca el candado en todos los endpoint)
                // de esta forma hay que documentar los endpoint privados con security dentro de @Operation
//                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                ;
    }

    /* Para orden personalizado de los @Tag */
    @Bean
    public OpenApiCustomizer sortTags() {
        return openApi -> {
            List<Tag> tags = openApi.getTags();
            if (tags != null) {
                tags.sort(Comparator.comparingInt(tag -> {
                    switch (tag.getName()) {
                        case "Autenticación": return 1;
                        case "Ping": return 2;
                        default: return 99;
                    }
                }));
            }
        };
    }
}
