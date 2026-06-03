package de.dhbw.webeng.mealplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dhbw.webeng.mealplanner.model.Recipe;
import de.dhbw.webeng.mealplanner.service.RecipeService;
import de.dhbw.webeng.mealplanner.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecipeService service;

    @Test
    void getAll_returnsEmptyArrayWhenNoRecipes() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getById_whenExists_returns200WithRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipe.setTitle("Test Recipe");
        recipe.setCategory("Chicken");
        // 200:
        when(service.getById(1L)).thenReturn(recipe);
        // 404-Fall:
        when(service.getById(999L))
                .thenThrow(new ResourceNotFoundException("Recipe", 999L));

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Recipe"))
                .andExpect(jsonPath("$.category").value("Chicken"));
    }

    @Test
    void getById_whenNotExists_returns404() throws Exception {
        when(service.getById(999L)).thenThrow(new ResourceNotFoundException("Recipe", 999L));

        mockMvc.perform(get("/api/recipes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_withMissingTitle_returns400() throws Exception {
        String invalidJson = """
            {
              "category": "Chicken",
              "caloriesPerServing": 500
            }
            """;

        mockMvc.perform(post("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
