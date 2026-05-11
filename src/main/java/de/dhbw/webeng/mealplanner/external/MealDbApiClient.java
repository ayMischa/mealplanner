package de.dhbw.webeng.mealplanner.external;

import de.dhbw.webeng.mealplanner.external.dto.MealDbMeal;
import de.dhbw.webeng.mealplanner.external.dto.MealDbResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class MealDbApiClient {

    private final RestClient restClient;

    public MealDbApiClient(RestClient mealDbRestClient) {
        this.restClient = mealDbRestClient;
    }

    public Optional<MealDbMeal> findRandom() {
        MealDbResponse response = restClient.get()
                .uri("/random.php")
                .retrieve()
                .body(MealDbResponse.class);

        return firstMealOf(response);
    }

    public Optional<MealDbMeal> findById(String mealDbId) {
        MealDbResponse response = restClient.get()
                .uri("/lookup.php?i={id}", mealDbId)
                .retrieve()
                .body(MealDbResponse.class);

        return firstMealOf(response);
    }

    public List<MealDbMeal> searchByName(String name) {
        MealDbResponse response = restClient.get()
                .uri("/search.php?s={name}", name)
                .retrieve()
                .body(MealDbResponse.class);

        if (response == null || response.meals() == null) {
            return List.of();
        }
        return response.meals();
    }

    private Optional<MealDbMeal> firstMealOf(MealDbResponse response) {
        if (response == null || response.meals() == null || response.meals().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(response.meals().getFirst());
    }
}