package com.ericlam404.bank_application;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Bank Application API",
                version = "1.0",
                description = "A simple bank application API"
        ),
        externalDocs = @ExternalDocumentation(
                description = "Back App Documentation",
                url = "https://github.com/EricLam404/bank-app"
        )
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
