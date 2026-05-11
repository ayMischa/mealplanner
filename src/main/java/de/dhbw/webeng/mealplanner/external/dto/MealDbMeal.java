package de.dhbw.webeng.mealplanner.external.dto;

public record MealDbMeal(
        String idMeal,
        String strMeal,
        String strCategory,
        String strArea,
        String strInstructions,
        String strMealThumb
) {}