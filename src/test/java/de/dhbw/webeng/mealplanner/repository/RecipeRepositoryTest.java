package de.dhbw.webeng.mealplanner.repository;

import de.dhbw.webeng.mealplanner.model.Recipe;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository repository;

    @Test
    void save_persistsRecipeAndGeneratesId() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Test Recipe");
        recipe.setCategory("Test");

        Recipe saved = repository.save(recipe);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Test Recipe");
    }

    @Test
    void findByMealDbId_whenExists_returnsRecipe() {
        Recipe recipe = new Recipe();
        recipe.setTitle("Imported Recipe");
        recipe.setMealDbId("52772");
        repository.save(recipe);

        Optional<Recipe> found = repository.findByMealDbId("52772");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Imported Recipe");
    }

    @Test
    void findByMealDbId_whenNotExists_returnsEmpty() {
        Optional<Recipe> found = repository.findByMealDbId("does-not-exist");

        assertThat(found).isEmpty();
    }
}