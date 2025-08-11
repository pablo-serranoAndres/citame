package com.milibrodereservas.citame.swagger;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/* Esta clase va a hacer que se sirva el contenido static de swagger-ui
 * en la direccion /swagger-ui/index.html desde la carpeta local
 * ./swagger-ui/node_modules/swagger-ui-dist/
 */

@Configuration
public class SwaggerStaticConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("file:./swagger-ui/node_modules/swagger-ui-dist/")
                .setCachePeriod(3600);
    }
}
