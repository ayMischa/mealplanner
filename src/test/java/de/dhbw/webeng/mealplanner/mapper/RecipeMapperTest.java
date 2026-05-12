package de.dhbw.webeng.mealplanner.mapper;

import de.dhbw.webeng.mealplanner.dto.RecipeRequest;
import de.dhbw.webeng.mealplanner.dto.RecipeResponse;
import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import de.dhbw.webeng.mealplanner.model.Recipe;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeMapperTest {

    @Test
    void toEntity_mapsAllFields() {
        // Arrange
        RecipeRequest request = new RecipeRequest(
                "Chicken & Rice", "Chicken", "American",
                "https://example.com/img.jpg",
                "Cook the chicken. Cook the rice.",
                "12345",
                450.0, 35.0, 55.0, 8.0
        );

        // Act
        Recipe recipe = RecipeMapper.toEntity(request);

        // Assert
        assertThat(recipe.getTitle()).isEqualTo("Chicken & Rice");
        assertThat(recipe.getCategory()).isEqualTo("Chicken");
        assertThat(recipe.getCaloriesPerServing()).isEqualTo(450.0);
        assertThat(recipe.getProteinG()).isEqualTo(35.0);
        assertThat(recipe.getMealDbId()).isEqualTo("12345");
    }

    @Test
    void toResponse_mapsAllFields() {
        Recipe recipe = new Recipe();
        recipe.setId(42L);
        recipe.setTitle("Pad Thai");
        recipe.setCategory("Noodles");
        recipe.setCaloriesPerServing(550.0);

        RecipeResponse response = RecipeMapper.toResponse(recipe);

        assertThat(response.id()).isEqualTo(42L);
        assertThat(response.title()).isEqualTo("Pad Thai");
        assertThat(response.category()).isEqualTo("Noodles");
        assertThat(response.caloriesPerServing()).isEqualTo(550.0);
    }

    @Test
    void fromMealDb_mapsExternalFieldsAndLeavesMacrosNull() {
        MealDbMeal external = new MealDbMeal(
                "52772",
                "Teriyaki Chicken Casserole",
                "Chicken",
                "Japanese",
                "Preheat oven...",
                "https://example.com/teri.jpg"
        );

        Recipe recipe = RecipeMapper.fromMealDb(external);

        assertThat(recipe.getMealDbId()).isEqualTo("52772");
        assertThat(recipe.getTitle()).isEqualTo("Teriyaki Chicken Casserole");
        assertThat(recipe.getCategory()).isEqualTo("Chicken");
        assertThat(recipe.getCaloriesPerServing()).isNull();
        assertThat(recipe.getProteinG()).isNull();
    }
}