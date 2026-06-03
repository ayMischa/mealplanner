const BASE = 'http://localhost:8080/api';

// Für Responses mit JSON-Body (GET, POST, PUT)
async function handleJson(response) {
    if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
    }
    return response.json();
}

// Für Responses ohne Body (DELETE → 204). 404 wird toleriert (war schon weg).
async function handleEmpty(response) {
    if (!response.ok && response.status !== 404) {
        throw new Error(`HTTP ${response.status}`);
    }
}

// --- Recipes ---

export function getRecipes() {
    return fetch(`${BASE}/recipes`).then(handleJson);
}

export function getRecipe(id) {
    return fetch(`${BASE}/recipes/${id}`).then(handleJson);
}

export function createRecipe(data) {
    return fetch(`${BASE}/recipes`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    }).then(handleJson);
}

export function deleteRecipe(id) {
    return fetch(`${BASE}/recipes/${id}`, { method: 'DELETE' }).then(handleEmpty);
}

export function importRecipeFromMealDb(mealDbId) {
    return fetch(`${BASE}/recipes/from-mealdb/${mealDbId}`, {
        method: 'POST',
    }).then(handleJson);
}

// --- Meal Plans ---

export function getMealPlans() {
    return fetch(`${BASE}/meal-plans`).then(handleJson);
}

export function getMealPlan(id) {
    return fetch(`${BASE}/meal-plans/${id}`).then(handleJson);
}

export function createMealPlan(data) {
    return fetch(`${BASE}/meal-plans`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    }).then(handleJson);
}

export function deleteMealPlan(id) {
    return fetch(`${BASE}/meal-plans/${id}`, { method: 'DELETE' }).then(handleEmpty);
}

// --- Planned Meals ---

export function getPlannedMeals(mealPlanId) {
    return fetch(`${BASE}/planned-meals?mealPlanId=${mealPlanId}`).then(handleJson);
}

export function createPlannedMeal(data) {
    return fetch(`${BASE}/planned-meals`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data),
    }).then(handleJson);
}

export function deletePlannedMeal(id) {
    return fetch(`${BASE}/planned-meals/${id}`, { method: 'DELETE' }).then(handleEmpty);
}

// --- MealDB ---

export function searchMealDb(query) {
    return fetch(`${BASE}/mealdb?q=${encodeURIComponent(query)}`).then(handleJson);
}

export function getRandomMeal() {
    return fetch(`${BASE}/mealdb/random`).then(handleJson);
}