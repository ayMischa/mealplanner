package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.external.MealDbApiClient;
import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import de.dhbw.webeng.mealplanner.mapper.RecipeMapper;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository repository;
    private final MealDbApiClient mealDbApiClient;

    public RecipeService(RecipeRepository repository, MealDbApiClient mealDbApiClient) {
        this.repository = repository;
        this.mealDbApiClient = mealDbApiClient;
    }

    public List<Recipe> findAll() {
        return repository.findAll();
    }

    public Optional<Recipe> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Recipe> findByMealDbId(String mealDbId) {
        return repository.findByMealDbId(mealDbId);
    }

    public Recipe create(Recipe recipe) {
        recipe.setId(null);
        return repository.save(recipe);
    }

    public Optional<Recipe> update(Long id, Recipe updated) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(updated.getTitle());
            existing.setCategory(updated.getCategory());
            existing.setArea(updated.getArea());
            existing.setImageUrl(updated.getImageUrl());
            existing.setInstructions(updated.getInstructions());
            existing.setMealDbId(updated.getMealDbId());
            existing.setCaloriesPerServing(updated.getCaloriesPerServing());
            existing.setProteinG(updated.getProteinG());
            existing.setCarbsG(updated.getCarbsG());
            existing.setFatG(updated.getFatG());
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

    public Recipe importFromMealDb(String mealDbId) {
        return repository.findByMealDbId(mealDbId)
                .orElseGet(() -> {
                    MealDbMeal external = mealDbApiClient.findById(mealDbId)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Recipe with TheMealDB id " + mealDbId + " not found"));
                    Recipe recipe = RecipeMapper.fromMealDb(external);
                    return repository.save(recipe);
                });
    }
}