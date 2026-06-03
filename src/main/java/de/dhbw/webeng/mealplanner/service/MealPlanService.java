package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.model.MealPlan;
import de.dhbw.webeng.mealplanner.model.MealPlanGoal;
import de.dhbw.webeng.mealplanner.repository.MealPlanRepository;
import de.dhbw.webeng.mealplanner.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return repository.save(plan);
    }

    public MealPlan update(Long id, MealPlan updated) {
        MealPlan existing = getById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setGoal(updated.getGoal());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MealPlan", id);
        }
        repository.deleteById(id);
    }
}