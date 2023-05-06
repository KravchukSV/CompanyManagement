package com.example.companymanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Company Management",
                description = "RESTfull API to manage the company",
                version = "v.1.0",
                contact = @Contact(
                        name = "Serhii",
                        email = "kravchuk.s.v.1@gmail.com"
                )
        )
)
@SpringBootApplication
public class CompanyManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyManagementApplication.class, args);
    }
}
