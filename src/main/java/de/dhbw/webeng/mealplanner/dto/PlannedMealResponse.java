package de.dhbw.webeng.mealplanner.dto;

import de.dhbw.webeng.mealplanner.model.MealType;

import java.time.LocalDate;

public record PlannedMealResponse(
        Long id,
        LocalDate date,
        MealType mealType,
        MealPlanResponse mealPlan,
        RecipeResponse recipe
) {}