package de.dhbw.webeng.mealplanner.repository;

import de.dhbw.webeng.mealplanner.model.PlannedMeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlannedMealRepository extends JpaRepository<PlannedMeal, Long> {

    List<PlannedMeal> findByMealPlanId(Long mealPlanId);

    List<PlannedMeal> findByMealPlanIdAndDate(Long mealPlanId, LocalDate date);
}