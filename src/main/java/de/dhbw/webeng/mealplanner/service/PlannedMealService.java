package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.model.MealPlan;
import de.dhbw.webeng.mealplanner.model.PlannedMeal;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.MealPlanRepository;
import de.dhbw.webeng.mealplanner.repository.PlannedMealRepository;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlannedMealService {

    private final PlannedMealRepository repository;
    private final MealPlanRepository mealPlanRepository;
    private final RecipeRepository recipeRepository;

    public PlannedMealService(
            PlannedMealRepository repository,
            MealPlanRepository mealPlanRepository,
            RecipeRepository recipeRepository) {
        this.repository = repository;
        this.mealPlanRepository = mealPlanRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<PlannedMeal> findAll() {
        return repository.findAll();
    }

    public Optional<PlannedMeal> findById(Long id) {
        return repository.findById(id);
    }

    public List<PlannedMeal> findByMealPlanId(Long mealPlanId) {
        return repository.findByMealPlanId(mealPlanId);
    }

    public List<PlannedMeal> findByMealPlanIdAndDate(Long mealPlanId, LocalDate date) {
        return repository.findByMealPlanIdAndDate(mealPlanId, date);
    }

    public PlannedMeal create(PlannedMeal meal, Long mealPlanId, Long recipeId) {
        MealPlan plan = loadMealPlan(mealPlanId);
        Recipe recipe = loadRecipe(recipeId);
        meal.setId(null);
        meal.setMealPlan(plan);
        meal.setRecipe(recipe);
        return repository.save(meal);
    }

    public Optional<PlannedMeal> update(Long id, PlannedMeal updated, Long mealPlanId, Long recipeId) {
        return repository.findById(id).map(existing -> {
            MealPlan plan = loadMealPlan(mealPlanId);
            Recipe recipe = loadRecipe(recipeId);
            existing.setDate(updated.getDate());
            existing.setMealType(updated.getMealType());
            existing.setMealPlan(plan);
            existing.setRecipe(recipe);
            return repository.save(existing);
        });
    }

    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    private MealPlan loadMealPlan(Long id) {
        return mealPlanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "MealPlan with id " + id + " does not exist"));
    }

    private Recipe loadRecipe(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Recipe with id " + id + " does not exist"));
    }
}