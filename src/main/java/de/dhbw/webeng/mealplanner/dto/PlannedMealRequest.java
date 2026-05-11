package de.dhbw.webeng.mealplanner.dto;

import de.dhbw.webeng.mealplanner.model.MealType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PlannedMealRequest(

        @NotNull
        LocalDate date,

        @NotNull
        MealType mealType,

        @NotNull
        Long mealPlanId,

        @NotNull
        Long recipeId

) {}