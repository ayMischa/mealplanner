package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
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
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        updated.setId(id);
        return Optional.of(repository.save(updated));
    }

    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}