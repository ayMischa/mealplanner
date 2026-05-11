package de.dhbw.webeng.mealplanner.mapper;

import de.dhbw.webeng.mealplanner.dto.MealPlanRequest;
import de.dhbw.webeng.mealplanner.dto.MealPlanResponse;
import de.dhbw.webeng.mealplanner.model.MealPlan;

public final class MealPlanMapper {

    private MealPlanMapper() {
    }

    public static MealPlan toEntity(MealPlanRequest request) {
        MealPlan plan = new MealPlan();
        plan.setName(request.name());
        plan.setDescription(request.description());
        plan.setGoal(request.goal());
        plan.setStartDate(request.startDate());
        plan.setEndDate(request.endDate());
        return plan;
    }

    public static MealPlanResponse toResponse(MealPlan plan) {
        return new MealPlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getGoal(),
                plan.getStartDate(),
                plan.getEndDate()
        );
    }
}