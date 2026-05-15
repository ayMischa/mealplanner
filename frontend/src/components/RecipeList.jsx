import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

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

    async function handleDelete(id) {
        if (!confirm('Rezept wirklich löschen?')) return;

        try {
            const response = await fetch(`http://localhost:8080/api/recipes/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok && response.status !== 404) {
                throw new Error(`HTTP ${response.status}`);
            }
            setRecipes(recipes.filter(r => r.id !== id));
        } catch (err) {
            alert('Fehler beim Löschen: ' + err.message);
        }
    }

    let content;
    if (loading) {
        content = <p>Lädt Rezepte...</p>;
    } else if (error) {
        content = <p style={{ color: 'crimson' }}>Fehler beim Laden: {error}</p>;
    } else if (recipes.length === 0) {
        content = <p>Noch keine Rezepte. Importiere eines aus TheMealDB.</p>;
    } else {
        content = (
            <ul style={{ listStyle: 'none', padding: 0 }}>
                {recipes.map(recipe => (
                    <li
                        key={recipe.id}
                        style={{
                            border: '1px solid #ddd',
                            borderRadius: '8px',
                            padding: '1rem',
                            marginBottom: '0.75rem',
                            position: 'relative'
                        }}
                    >
                        <button
                            onClick={() => handleDelete(recipe.id)}
                            aria-label="Löschen"
                            style={{
                                position: 'absolute',
                                top: '0.5rem',
                                right: '0.5rem',
                                background: 'transparent',
                                border: 'none',
                                fontSize: '1.25rem',
                                color: '#999',
                                cursor: 'pointer',
                                padding: '0.25rem 0.5rem',
                                borderRadius: '4px'
                            }}
                        >
                            ✕
                        </button>
                        <Link
                            to={`/recipes/${recipe.id}`}
                            style={{ textDecoration: 'none', color: 'inherit', display: 'block', paddingRight: '2rem' }}
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
                        </Link>
                    </li>
                ))}
            </ul>
        );
    }

    return (
        <div>
            <div style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                marginBottom: '1rem'
            }}>
                <h2 style={{ margin: 0 }}>Rezepte</h2>
                <Link
                    to="/recipes/new"
                    style={{
                        padding: '0.5rem 1rem',
                        background: '#2563eb',
                        color: 'white',
                        textDecoration: 'none',
                        borderRadius: '6px',
                        fontSize: '0.9rem'
                    }}
                >
                    + Neues Rezept
                </Link>
            </div>
            {content}
        </div>
    );
}

export default RecipeList;