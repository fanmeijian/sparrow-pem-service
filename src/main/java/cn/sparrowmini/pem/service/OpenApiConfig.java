package cn.sparrowmini.pem.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	/**
	 * 
	 * @return OpenApi
	 */
	@Bean
	public OpenAPI springShopOpenAPI() {
//		SecurityScheme securityScheme = new SecurityScheme();
//		securityScheme.setScheme("bearer");
//		securityScheme.setType(Type.HTTP);
//		securityScheme.setBearerFormat("JWT");
		
		return new OpenAPI()
//				.components(new Components().addSecuritySchemes("bearerAuth",securityScheme ))
//				.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
				.info(new Info().title("Sparrow Permission Service")
				.description("权限服务").version("v0.0.1"));
	}
}
