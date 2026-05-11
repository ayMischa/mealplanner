package de.dhbw.webeng.mealplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record RecipeRequest(

        @NotBlank
        String title,

        String category,
        String area,
        String imageUrl,
        String instructions,
        String mealDbId,

        @PositiveOrZero
        Double caloriesPerServing,

        @PositiveOrZero
        Double proteinG,

        @PositiveOrZero
        Double carbsG,

        @PositiveOrZero
        Double fatG

) {}