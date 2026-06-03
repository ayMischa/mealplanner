package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.external.MealDbApiClient;
import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import de.dhbw.webeng.mealplanner.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository repository;

    @Mock
    private MealDbApiClient mealDbApiClient;

    @InjectMocks
    private RecipeService service;

    @Test
    void findAll_returnsAllRecipesFromRepository() {
        Recipe r1 = new Recipe();
        Recipe r2 = new Recipe();
        when(repository.findAll()).thenReturn(List.of(r1, r2));

        List<Recipe> result = service.findAll();

        assertThat(result).hasSize(2).containsExactly(r1, r2);
    }

    @Test
    void getById_whenExists_returnsRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setTitle("Test");
        when(repository.findById(1L)).thenReturn(Optional.of(recipe));

        Recipe result = service.getById(1L);

        assertThat(result).isEqualTo(recipe);
    }

    @Test
    void getById_whenNotExists_throwsNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void update_whenExists_modifiesAndReturnsUpdated() {
        Recipe existing = new Recipe();
        existing.setId(1L);
        existing.setTitle("Old Title");

        Recipe updated = new Recipe();
        updated.setTitle("New Title");
        updated.setCategory("New Category");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0));

        Recipe result = service.update(1L, updated);

        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getCategory()).isEqualTo("New Category");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void update_whenNotExists_throwsNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(999L, new Recipe()))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void create_nullsOutIdBeforeSaving() {
        Recipe recipe = new Recipe();
        recipe.setId(123L);
        recipe.setTitle("Test");
        when(repository.save(any(Recipe.class))).thenReturn(recipe);

        service.create(recipe);

        assertThat(recipe.getId()).isNull();
        verify(repository).save(recipe);
    }

    @Test
    void importFromMealDb_whenAlreadyExists_returnsExistingWithoutCallingApi() {
        Recipe existing = new Recipe();
        existing.setMealDbId("52772");
        when(repository.findByMealDbId("52772")).thenReturn(Optional.of(existing));

        Recipe result = service.importFromMealDb("52772");

        assertThat(result).isSameAs(existing);
        verify(mealDbApiClient, never()).findById(any());
        verify(repository, never()).save(any());
    }

    @Test
    void importFromMealDb_whenNotInDb_fetchesFromApiAndSaves() {
        MealDbMeal external = new MealDbMeal(
                "52772", "Teriyaki", "Chicken", "Japanese",
                "Instructions...", "image.jpg"
        );
        when(repository.findByMealDbId("52772")).thenReturn(Optional.empty());
        when(mealDbApiClient.findById("52772")).thenReturn(Optional.of(external));
        when(repository.save(any(Recipe.class))).thenAnswer(inv -> inv.getArgument(0));

        Recipe result = service.importFromMealDb("52772");

        assertThat(result.getTitle()).isEqualTo("Teriyaki");
        assertThat(result.getMealDbId()).isEqualTo("52772");
        verify(repository).save(any(Recipe.class));
    }

    @Test
    void importFromMealDb_whenNotFoundOnMealDb_throws404() {
        when(repository.findByMealDbId("99999999")).thenReturn(Optional.empty());
        when(mealDbApiClient.findById("99999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.importFromMealDb("99999999"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
