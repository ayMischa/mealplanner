package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.model.MealPlan;
import de.dhbw.webeng.mealplanner.model.PlannedMeal;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.MealPlanRepository;
import de.dhbw.webeng.mealplanner.repository.PlannedMealRepository;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import de.dhbw.webeng.mealplanner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public PlannedMeal getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PlannedMeal", id));
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

    public PlannedMeal update(Long id, PlannedMeal updated, Long mealPlanId, Long recipeId) {
        PlannedMeal existing = getById(id);
        MealPlan plan = loadMealPlan(mealPlanId);
        Recipe recipe = loadRecipe(recipeId);
        existing.setDate(updated.getDate());
        existing.setMealType(updated.getMealType());
        existing.setMealPlan(plan);
        existing.setRecipe(recipe);
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PlannedMeal", id);
        }
        repository.deleteById(id);
    }

    private MealPlan loadMealPlan(Long mealPlanId) {
        return mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new ResourceNotFoundException("MealPlan", mealPlanId));
    }

    private Recipe loadRecipe(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", recipeId));
    }
}
