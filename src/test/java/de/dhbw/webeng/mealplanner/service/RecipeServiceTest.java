package de.dhbw.webeng.mealplanner.service;

import de.dhbw.webeng.mealplanner.external.MealDbApiClient;
import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

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
    void findById_whenNotExists_returnsEmpty() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<Recipe> result = service.findById(999L);

        assertThat(result).isEmpty();
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
        when(repository.findByMealDbId("00000")).thenReturn(Optional.empty());
        when(mealDbApiClient.findById("00000")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.importFromMealDb("00000"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("00000");
    }
}