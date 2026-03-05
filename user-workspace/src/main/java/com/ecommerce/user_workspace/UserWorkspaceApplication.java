package com.ecommerce.user_workspace;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info=@Info(
                title = "User Service API",
                version = "1.0",
                description = "User management endpoints"
        )
)
public class UserWorkspaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserWorkspaceApplication.class, args);
	}

}
