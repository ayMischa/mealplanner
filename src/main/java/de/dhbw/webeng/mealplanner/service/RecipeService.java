package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.external.MealDbApiClient;
import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import de.dhbw.webeng.mealplanner.mapper.RecipeMapper;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import de.dhbw.webeng.mealplanner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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

    public Recipe getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
    }

    public Optional<Recipe> findByMealDbId(String mealDbId) {
        return repository.findByMealDbId(mealDbId);
    }

    public Recipe create(Recipe recipe) {
        recipe.setId(null);
        return repository.save(recipe);
    }

    public Recipe update(Long id, Recipe updated) {
        Recipe existing = getById(id);
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
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Recipe", id);
        }
        repository.deleteById(id);
    }

    public Recipe importFromMealDb(String mealDbId) {
        return repository.findByMealDbId(mealDbId)
                .orElseGet(() -> {
                    MealDbMeal external = mealDbApiClient.findById(mealDbId)
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "MealDB-Rezept " + mealDbId + " nicht gefunden"
                            ));

                    Recipe recipe = RecipeMapper.fromMealDb(external);
                    return repository.save(recipe);
                });
    }
}
