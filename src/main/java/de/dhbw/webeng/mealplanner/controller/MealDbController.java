package de.dhbw.webeng.mealplanner.controller;

import de.dhbw.webeng.mealplanner.external.MealDbApiClient;
import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mealdb")
public class MealDbController {

    private final MealDbApiClient client;

    public MealDbController(MealDbApiClient client) {
        this.client = client;
    }

    @GetMapping("/random")
    public ResponseEntity<MealDbMeal> random() {
        return client.findRandom()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDbMeal> getById(@PathVariable String id) {
        return client.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<MealDbMeal> search(@RequestParam String q) {
        return client.searchByName(q);
    }
}