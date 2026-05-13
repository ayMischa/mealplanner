import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';

function RecipeDetail() {
    const { id } = useParams();
    const [recipe, setRecipe] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchRecipe() {
            try {
                const response = await fetch(`http://localhost:8080/api/recipes/${id}`);
                if (response.status === 404) {
                    throw new Error('Rezept nicht gefunden');
                }
                if (!response.ok) {
                    throw new Error(`HTTP ${response.status}`);
                }
                const data = await response.json();
                setRecipe(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        fetchRecipe();
    }, [id]);

    if (loading) {
        return <p>Lädt Rezept...</p>;
    }

    if (error) {
        return (
            <div>
                <p style={
                    {
                        color: 'crimson'
                    }
                }
                >Fehler: {error}</p>
                <Link to="/">← Zurück zur Liste</Link>
            </div>
        );
    }

    return (
        <div>
            <Link to="/" style={{ display: 'inline-block', marginBottom: '1rem' }}>
                ← Zurück zur Liste
            </Link>

            <h2 style={{ marginTop: 0 }}>{recipe.title}</h2>

            {recipe.imageUrl && (
                <img
                    src={recipe.imageUrl}
                    alt={recipe.title}
                    style={{ maxWidth: '400px', borderRadius: '8px', marginBottom: '1rem' }}
                />
            )}

            <div style={{ color: '#666', marginBottom: '1rem' }}>
                {recipe.category && <span>{recipe.category}</span>}
                {recipe.category && recipe.area && <span> · </span>}
                {recipe.area && <span>{recipe.area}</span>}
            </div>

            {(recipe.caloriesPerServing || recipe.proteinG) && (
                <div style={{
                    padding: '1rem',
                    background: 'rgba(128,128,128,0.1)',
                    borderRadius: '8px',
                    marginBottom: '1rem'
                }}>
                    <strong>Nährwerte pro Portion</strong>
                    <div style={{ marginTop: '0.5rem' }}>
                        {recipe.caloriesPerServing && <div>{recipe.caloriesPerServing} kcal</div>}
                        {recipe.proteinG && <div>Protein: {recipe.proteinG}g</div>}
                        {recipe.carbsG && <div>Kohlenhydrate: {recipe.carbsG}g</div>}
                        {recipe.fatG && <div>Fett: {recipe.fatG}g</div>}
                    </div>
                </div>
            )}

            {recipe.instructions && (
                <div>
                    <h3>Zubereitung</h3>
                    <p style={{ whiteSpace: 'pre-wrap', lineHeight: 1.6 }}>{recipe.instructions}</p>
                </div>
            )}
        </div>
    );
}

export default RecipeDetail;