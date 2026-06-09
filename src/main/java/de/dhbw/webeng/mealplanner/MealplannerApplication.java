package de.dhbw.webeng.mealplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(
        title = "Mealplanner API",
        version = "1.0.0",
        description = "REST-API für Rezeptverwaltung, Ernährungspläne und TheMealDB-Integration"
))

@SpringBootApplication
public class MealplannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealplannerApplication.class, args);
    }

}
