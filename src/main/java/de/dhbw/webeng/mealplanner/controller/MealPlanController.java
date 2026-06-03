package de.dhbw.webeng.mealplanner.controller;

import de.dhbw.webeng.mealplanner.dto.MealPlanRequest;
import de.dhbw.webeng.mealplanner.dto.MealPlanResponse;
import de.dhbw.webeng.mealplanner.mapper.MealPlanMapper;
import de.dhbw.webeng.mealplanner.model.MealPlan;
import de.dhbw.webeng.mealplanner.model.MealPlanGoal;
import de.dhbw.webeng.mealplanner.service.MealPlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/meal-plans")
public class MealPlanController {

    private final MealPlanService service;

    public MealPlanController(MealPlanService service) {
        this.service = service;
    }

    @GetMapping
    public List<MealPlanResponse> getAll(
            @RequestParam(required = false) MealPlanGoal goal) {
        List<MealPlan> plans = (goal != null)
                ? service.findByGoal(goal)
                : service.findAll();
        return plans.stream()
                .map(MealPlanMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MealPlanResponse getById(@PathVariable Long id) {
        return MealPlanMapper.toResponse(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<MealPlanResponse> create(
            @Valid @RequestBody MealPlanRequest request) {
        MealPlan created = service.create(MealPlanMapper.toEntity(request));
        MealPlanResponse response = MealPlanMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("/api/meal-plans/" + created.getId()))
                .body(response);
    }

    @PutMapping("/{id}")
    public MealPlanResponse update(@PathVariable Long id, @Valid @RequestBody MealPlanRequest request) {
        MealPlan updated = service.update(id, MealPlanMapper.toEntity(request));
        return MealPlanMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}