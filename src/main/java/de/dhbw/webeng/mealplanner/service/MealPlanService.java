package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.model.MealPlan;
import de.dhbw.webeng.mealplanner.model.MealPlanGoal;
import de.dhbw.webeng.mealplanner.repository.MealPlanRepository;
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

    public Optional<MealPlan> findById(Long id) {
        return repository.findById(id);
    }

    public MealPlan create(MealPlan plan) {
        plan.setId(null);
        return repository.save(plan);
    }

    public Optional<MealPlan> update(Long id, MealPlan updated) {
        return repository.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setGoal(updated.getGoal());
            existing.setStartDate(updated.getStartDate());
            existing.setEndDate(updated.getEndDate());
            return repository.save(existing);
        });
    }

    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}