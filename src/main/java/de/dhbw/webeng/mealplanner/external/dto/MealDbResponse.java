package de.dhbw.webeng.mealplanner.external.dto;

import java.util.List;

public record MealDbResponse(
        List<MealDbMeal> meals
) {}