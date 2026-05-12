import { useState, useEffect } from 'react';

function RecipeList() {
    const [recipes, setRecipes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchRecipes() {
            try {
                const response = await fetch('http://localhost:8080/api/recipes');
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}`);
                }
                const data = await response.json();
                setRecipes(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        fetchRecipes();
    }, []);

    if (loading) {
        return <p>Lädt Rezepte...</p>;
    }

    if (error) {
        return <p style={{ color: 'crimson' }}>Fehler beim Laden: {error}</p>;
    }

    if (recipes.length === 0) {
        return <p>Noch keine Rezepte. Importiere eines aus TheMealDB oder lege eins per API an.</p>;
    }

    return (
        <ul style={{ listStyle: 'none', padding: 0 }}>
            {recipes.map(recipe => (
                <li
                    key={recipe.id}
                    style={{
                        border: '1px solid #ddd',
                        borderRadius: '8px',
                        padding: '1rem',
                        marginBottom: '0.75rem'
                    }}
                >
                    <h3 style={{ margin: '0 0 0.5rem 0' }}>{recipe.title}</h3>
                    <div style={{ color: '#666', fontSize: '0.9rem' }}>
                        {recipe.category && <span>{recipe.category}</span>}
                        {recipe.category && recipe.area && <span> · </span>}
                        {recipe.area && <span>{recipe.area}</span>}
                    </div>
                    {recipe.caloriesPerServing && (
                        <div style={{ marginTop: '0.5rem', fontSize: '0.9rem' }}>
                            {recipe.caloriesPerServing} kcal
                            {recipe.proteinG && ` · ${recipe.proteinG}g Protein`}
                        </div>
                    )}
                </li>
            ))}
        </ul>
    );
}

export default RecipeList;