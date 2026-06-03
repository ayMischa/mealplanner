package de.dhbw.webeng.mealplanner.controller;

import de.dhbw.webeng.mealplanner.dto.RecipeRequest;
import de.dhbw.webeng.mealplanner.dto.RecipeResponse;
import de.dhbw.webeng.mealplanner.mapper.RecipeMapper;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public List<RecipeResponse> getAll() {
        return service.findAll().stream()
                .map(RecipeMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public RecipeResponse getById(@PathVariable Long id) {
        return RecipeMapper.toResponse(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> create(@Valid @RequestBody RecipeRequest request) {
        Recipe created = service.create(RecipeMapper.toEntity(request));
        RecipeResponse response = RecipeMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("/api/recipes/" + created.getId()))
                .body(response);
    }

    @PostMapping("/from-mealdb/{mealDbId}")
    public ResponseEntity<RecipeResponse> importFromMealDb(@PathVariable String mealDbId) {
        Recipe imported = service.importFromMealDb(mealDbId);
        RecipeResponse response = RecipeMapper.toResponse(imported);
        return ResponseEntity
                .created(URI.create("/api/recipes/" + imported.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public RecipeResponse update(@PathVariable Long id, @Valid @RequestBody RecipeRequest request) {
        Recipe updated = service.update(id, RecipeMapper.toEntity(request));
        return RecipeMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}