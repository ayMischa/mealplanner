package de.dhbw.webeng.mealplanner.dto;

import de.dhbw.webeng.mealplanner.model.MealPlanGoal;

import java.time.LocalDate;

public record MealPlanResponse(
        Long id,
        String name,
        String description,
        MealPlanGoal goal,
        LocalDate startDate,
        LocalDate endDate
) {}