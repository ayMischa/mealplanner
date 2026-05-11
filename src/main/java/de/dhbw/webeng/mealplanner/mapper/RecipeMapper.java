package de.dhbw.webeng.mealplanner.mapper;

import de.dhbw.webeng.mealplanner.dto.RecipeRequest;
import de.dhbw.webeng.mealplanner.dto.RecipeResponse;
import de.dhbw.webeng.mealplanner.model.Recipe;

public final class RecipeMapper {

    private RecipeMapper() {
    }

    public static Recipe toEntity(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setTitle(request.title());
        recipe.setCategory(request.category());
        recipe.setArea(request.area());
        recipe.setImageUrl(request.imageUrl());
        recipe.setInstructions(request.instructions());
        recipe.setMealDbId(request.mealDbId());
        recipe.setCaloriesPerServing(request.caloriesPerServing());
        recipe.setProteinG(request.proteinG());
        recipe.setCarbsG(request.carbsG());
        recipe.setFatG(request.fatG());
        return recipe;
    }

    public static RecipeResponse toResponse(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getCategory(),
                recipe.getArea(),
                recipe.getImageUrl(),
                recipe.getInstructions(),
                recipe.getMealDbId(),
                recipe.getCaloriesPerServing(),
                recipe.getProteinG(),
                recipe.getCarbsG(),
                recipe.getFatG()
        );
    }
}