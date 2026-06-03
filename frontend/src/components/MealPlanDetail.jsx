import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { getMealPlan, getPlannedMeals, getRecipes, createPlannedMeal, deletePlannedMeal } from '../api';

const MEAL_TYPE_ORDER = ['BREAKFAST', 'LUNCH', 'DINNER', 'SNACK'];
const MEAL_TYPE_LABELS = {
    BREAKFAST: 'Frühstück',
    LUNCH: 'Mittag',
    DINNER: 'Abend',
    SNACK: 'Snack'
};

function MealPlanDetail() {
    const { id } = useParams();

    const [plan, setPlan] = useState(null);
    const [meals, setMeals] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    // Form state
    const [form, setForm] = useState({
        date: '',
        mealType: 'LUNCH',
        recipeId: ''
    });
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        async function loadAll() {
            try {
                const [planData, mealsData, recipesData] = await Promise.all([
                    getMealPlan(id),
                    getPlannedMeals(id),
                    getRecipes()
                ]);
                setPlan(planData);
                setMeals(mealsData);
                setRecipes(recipesData);
            } catch (err) {
                setError(err.message.includes('404') ? 'Plan nicht gefunden' : err.message);
            } finally {
                setLoading(false);
            }
        }
        loadAll();
    }, [id]);

    function handleFormChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleAddMeal(e) {
        e.preventDefault();
        setSubmitting(true);
        try {
            const created = await createPlannedMeal({
                date: form.date,
                mealType: form.mealType,
                mealPlanId: parseInt(id),
                recipeId: parseInt(form.recipeId)
            });
            setMeals([...meals, created]);
            setForm({ date: '', mealType: 'LUNCH', recipeId: '' });
        } catch (err) {
            alert('Fehler: ' + err.message);
        } finally {
            setSubmitting(false);
        }
    }

    async function handleDeleteMeal(mealId) {
        if (!confirm('Mahlzeit löschen?')) return;
        try {
            await deletePlannedMeal(mealId);
            setMeals(meals.filter(m => m.id !== mealId));
        } catch (err) {
            alert('Fehler beim Löschen: ' + err.message);
        }
    }

    if (loading) return <p>Lädt Plan...</p>;
    if (error) {
        return (
            <div>
                <p style={{ color: 'crimson' }}>Fehler: {error}</p>
                <Link to="/meal-plans">← Zurück zur Liste</Link>
            </div>
        );
    }

    // Mahlzeiten nach Datum gruppieren
    const groupedByDate = {};
    meals.forEach(m => {
        if (!groupedByDate[m.date]) groupedByDate[m.date] = [];
        groupedByDate[m.date].push(m);
    });
    const sortedDates = Object.keys(groupedByDate).sort();
    sortedDates.forEach(date => {
        groupedByDate[date].sort((a, b) =>
            MEAL_TYPE_ORDER.indexOf(a.mealType) - MEAL_TYPE_ORDER.indexOf(b.mealType)
        );
    });

    const inputStyle = {
        padding: '0.5rem',
        fontSize: '0.95rem',
        boxSizing: 'border-box',
        width: '100%'
    };

    return (
        <div>
            <Link to="/meal-plans" style={{ display: 'inline-block', marginBottom: '1rem' }}>
                ← Zurück zur Liste
            </Link>

            <h2 style={{ marginTop: 0 }}>{plan.name}</h2>
            <div style={{ color: '#666', marginBottom: '0.5rem' }}>
                Ziel: <strong>{plan.goal}</strong>
            </div>
            <div style={{ color: '#666', marginBottom: '1rem' }}>
                {plan.startDate} – {plan.endDate}
            </div>
            {plan.description && (
                <p style={{ marginBottom: '2rem' }}>{plan.description}</p>
            )}

            <h3>Mahlzeiten</h3>
            {sortedDates.length === 0 ? (
                <p style={{ color: '#888' }}>Noch keine Mahlzeiten geplant.</p>
            ) : (
                sortedDates.map(date => (
                    <div key={date} style={{ marginBottom: '1.5rem' }}>
                        <h4 style={{ margin: '0 0 0.5rem 0' }}>{date}</h4>
                        <ul style={{ listStyle: 'none', padding: 0, margin: 0 }}>
                            {groupedByDate[date].map(meal => (
                                <li
                                    key={meal.id}
                                    style={{
                                        display: 'flex',
                                        justifyContent: 'space-between',
                                        alignItems: 'center',
                                        padding: '0.5rem 0.75rem',
                                        border: '1px solid #eee',
                                        borderRadius: '6px',
                                        marginBottom: '0.25rem'
                                    }}
                                >
                                    <div>
                                        <strong>{MEAL_TYPE_LABELS[meal.mealType]}:</strong>{' '}
                                        {meal.recipe.title}
                                        {meal.recipe.caloriesPerServing && (
                                            <span style={{ color: '#666', fontSize: '0.9rem' }}>
                        {' · '}{meal.recipe.caloriesPerServing} kcal
                      </span>
                                        )}
                                    </div>
                                    <button
                                        onClick={() => handleDeleteMeal(meal.id)}
                                        aria-label="Löschen"
                                        style={{
                                            background: 'transparent',
                                            border: 'none',
                                            fontSize: '1rem',
                                            color: '#999',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        ✕
                                    </button>
                                </li>
                            ))}
                        </ul>
                    </div>
                ))
            )}

            <h3 style={{ marginTop: '2rem' }}>Mahlzeit hinzufügen</h3>

            {recipes.length === 0 ? (
                <p style={{ color: '#888' }}>
                    Du brauchst mindestens ein Rezept, bevor du Mahlzeiten planen kannst.{' '}
                    <Link to="/recipes/new">Hier ein neues anlegen</Link>.
                </p>
            ) : (
                <form onSubmit={handleAddMeal}
                      style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 2fr auto', gap: '0.75rem', alignItems: 'end' }}>
                    <label>
                        Datum
                        <input
                            type="date"
                            name="date"
                            value={form.date}
                            onChange={handleFormChange}
                            min={plan.startDate}
                            max={plan.endDate}
                            required
                            style={inputStyle}
                        />
                    </label>
                    <label>
                        Mahlzeit
                        <select
                            name="mealType"
                            value={form.mealType}
                            onChange={handleFormChange}
                            required
                            style={inputStyle}
                        >
                            {MEAL_TYPE_ORDER.map(t => (
                                <option key={t} value={t}>{MEAL_TYPE_LABELS[t]}</option>
                            ))}
                        </select>
                    </label>
                    <label>
                        Rezept
                        <select
                            name="recipeId"
                            value={form.recipeId}
                            onChange={handleFormChange}
                            required
                            style={inputStyle}
                        >
                            <option value="">Bitte wählen...</option>
                            {recipes.map(r => (
                                <option key={r.id} value={r.id}>{r.title}</option>
                            ))}
                        </select>
                    </label>
                    <button
                        type="submit"
                        disabled={submitting}
                        style={{
                            padding: '0.5rem 1rem',
                            background: '#2563eb',
                            color: 'white',
                            border: 'none',
                            borderRadius: '6px',
                            cursor: submitting ? 'not-allowed' : 'pointer'
                        }}
                    >
                        {submitting ? '...' : 'Hinzufügen'}
                    </button>
                </form>
            )}
        </div>
    );
}

export default MealPlanDetail;
