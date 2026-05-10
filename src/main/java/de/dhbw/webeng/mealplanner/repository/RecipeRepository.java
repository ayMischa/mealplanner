package de.dhbw.webeng.mealplanner.repository;

import de.dhbw.webeng.mealplanner.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findByMealDbId(String mealDbId);
}
