package de.dhbw.webeng.mealplanner.dto;

import de.dhbw.webeng.mealplanner.model.MealPlanGoal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MealPlanRequest(

        @NotBlank
        String name,

        String description,

        @NotNull
        MealPlanGoal goal,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate

) {}