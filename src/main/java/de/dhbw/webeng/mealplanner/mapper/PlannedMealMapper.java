package de.dhbw.webeng.mealplanner.mapper;

import de.dhbw.webeng.mealplanner.dto.PlannedMealRequest;
import de.dhbw.webeng.mealplanner.dto.PlannedMealResponse;
import de.dhbw.webeng.mealplanner.model.PlannedMeal;

public final class PlannedMealMapper {

    private PlannedMealMapper() {
    }

    public static PlannedMeal toEntity(PlannedMealRequest request) {
        PlannedMeal meal = new PlannedMeal();
        meal.setDate(request.date());
        meal.setMealType(request.mealType());
        return meal;
    }

    public static PlannedMealResponse toResponse(PlannedMeal meal) {
        return new PlannedMealResponse(
                meal.getId(),
                meal.getDate(),
                meal.getMealType(),
                MealPlanMapper.toResponse(meal.getMealPlan()),
                RecipeMapper.toResponse(meal.getRecipe())
        );
    }
}