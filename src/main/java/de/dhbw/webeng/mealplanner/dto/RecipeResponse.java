package de.dhbw.webeng.mealplanner.dto;

public record RecipeResponse(
        Long id,
        String title,
        String category,
        String area,
        String imageUrl,
        String instructions,
        String mealDbId,
        Double caloriesPerServing,
        Double proteinG,
        Double carbsG,
        Double fatG
) {}