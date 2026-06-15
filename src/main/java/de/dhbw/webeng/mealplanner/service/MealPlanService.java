package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.model.MealPlan;
import de.dhbw.webeng.mealplanner.model.MealPlanGoal;
import de.dhbw.webeng.mealplanner.repository.MealPlanRepository;
import de.dhbw.webeng.mealplanner.exception.ResourceNotFoundException;
import de.dhbw.webeng.mealplanner.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealPlanService {

    private final MealPlanRepository repository;

    public MealPlanService(MealPlanRepository repository) {
        this.repository = repository;
    }

    public List<MealPlan> findAll() {
        return repository.findAll();
    }

    public List<MealPlan> findByGoal(MealPlanGoal goal) {
        return repository.findByGoal(goal);
    }

    public MealPlan getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MealPlan", id));
    }

    public MealPlan create(MealPlan plan) {
        plan.setId(null);
        validateDateRange(plan);
        return repository.save(plan);
    }

    public MealPlan update(Long id, MealPlan updated) {
        MealPlan existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setGoal(updated.getGoal());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        validateDateRange(existing);
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MealPlan", id);
        }
        repository.deleteById(id);
    }

    private void validateDateRange(MealPlan plan) {
        if (plan.getEndDate().isBefore(plan.getStartDate())) {
            throw new BadRequestException("Enddatum darf nicht vor dem Startdatum liegen");
        }
    }
}