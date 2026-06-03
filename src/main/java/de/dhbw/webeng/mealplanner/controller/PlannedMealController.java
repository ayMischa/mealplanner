package de.dhbw.webeng.mealplanner.controller;

import de.dhbw.webeng.mealplanner.dto.PlannedMealRequest;
import de.dhbw.webeng.mealplanner.dto.PlannedMealResponse;
import de.dhbw.webeng.mealplanner.mapper.PlannedMealMapper;
import de.dhbw.webeng.mealplanner.model.PlannedMeal;
import de.dhbw.webeng.mealplanner.service.PlannedMealService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planned-meals")
public class PlannedMealController {

    private final PlannedMealService service;

    public PlannedMealController(PlannedMealService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlannedMealResponse> getAll(
            @RequestParam(required = false) Long mealPlanId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<PlannedMeal> meals;
        if (mealPlanId != null && date != null) {
            meals = service.findByMealPlanIdAndDate(mealPlanId, date);
        } else if (mealPlanId != null) {
            meals = service.findByMealPlanId(mealPlanId);
        } else {
            meals = service.findAll();
        }
        return meals.stream()
                .map(PlannedMealMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PlannedMealResponse getById(@PathVariable Long id) {
        return PlannedMealMapper.toResponse(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<PlannedMealResponse> create(
            @Valid @RequestBody PlannedMealRequest request) {
        PlannedMeal entity = PlannedMealMapper.toEntity(request);
        PlannedMeal created = service.create(entity, request.mealPlanId(), request.recipeId());
        PlannedMealResponse response = PlannedMealMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("/api/planned-meals/" + created.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public PlannedMealResponse update(@PathVariable Long id, @Valid @RequestBody PlannedMealRequest request) {
        PlannedMeal updated = service.update(
                id,
                PlannedMealMapper.toEntity(request),
                request.mealPlanId(),
                request.recipeId());
        return PlannedMealMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
