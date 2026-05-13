import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function RecipeForm() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        title: '',
        category: '',
        area: '',
        instructions: '',
        caloriesPerServing: '',
        proteinG: '',
        carbsG: '',
        fatG: ''
    });
    const [submitting, setSubmitting] = useState(false);
    const [error, setError] = useState(null);

    function handleChange(e) {
        setForm({ ...form, [e.target.name]: e.target.value });
    }

    async function handleSubmit(e) {
        e.preventDefault();
        setSubmitting(true);
        setError(null);

        // Inputs sind immer Strings. Backend will Zahlen oder null.
        const payload = {
            title: form.title,
            category: form.category || null,
            area: form.area || null,
            instructions: form.instructions || null,
            caloriesPerServing: form.caloriesPerServing ? parseFloat(form.caloriesPerServing) : null,
            proteinG: form.proteinG ? parseFloat(form.proteinG) : null,
            carbsG: form.carbsG ? parseFloat(form.carbsG) : null,
            fatG: form.fatG ? parseFloat(form.fatG) : null
        };

        try {
            const response = await fetch('http://localhost:8080/api/recipes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Bitte Eingaben prüfen (z.B. Titel fehlt oder negativer Wert)');
                }
                throw new Error(`HTTP ${response.status}`);
            }

            navigate('/');
        } catch (err) {
            setError(err.message);
        } finally {
            setSubmitting(false);
        }
    }

    const inputStyle = {
        width: '100%',
        padding: '0.5rem',
        fontSize: '1rem',
        boxSizing: 'border-box',
        marginTop: '0.25rem'
    };

    return (
        <div>
            <Link to="/" style={{ display: 'inline-block', marginBottom: '1rem' }}>
                ← Zurück zur Liste
            </Link>

            <h2 style={{ marginTop: 0 }}>Neues Rezept</h2>

            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '1rem' }}>
                    <label>
                        Titel *
                        <input
                            type="text"
                            name="title"
                            value={form.title}
                            onChange={handleChange}
                            required
                            style={inputStyle}
                        />
                    </label>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '1rem' }}>
                    <label style={{ flex: 1 }}>
                        Kategorie
                        <input
                            type="text"
                            name="category"
                            value={form.category}
                            onChange={handleChange}
                            placeholder="z.B. Chicken, Beef"
                            style={inputStyle}
                        />
                    </label>
                    <label style={{ flex: 1 }}>
                        Herkunft
                        <input
                            type="text"
                            name="area"
                            value={form.area}
                            onChange={handleChange}
                            placeholder="z.B. Italian, Japanese"
                            style={inputStyle}
                        />
                    </label>
                </div>

                <div style={{ marginBottom: '1rem' }}>
                    <label>
                        Zubereitung
                        <textarea
                            name="instructions"
                            value={form.instructions}
                            onChange={handleChange}
                            rows={6}
                            style={inputStyle}
                        />
                    </label>
                </div>

                <fieldset style={{ marginBottom: '1rem', padding: '1rem', border: '1px solid #ddd', borderRadius: '8px' }}>
                    <legend>Nährwerte pro Portion</legend>
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(4, 1fr)', gap: '0.75rem' }}>
                        <label>
                            Kalorien
                            <input type="number" name="caloriesPerServing"
                                   value={form.caloriesPerServing} onChange={handleChange}
                                   min={0} style={inputStyle} />
                        </label>
                        <label>
                            Protein (g)
                            <input type="number" name="proteinG"
                                   value={form.proteinG} onChange={handleChange}
                                   min={0} step="0.1" style={inputStyle} />
                        </label>
                        <label>
                            Carbs (g)
                            <input type="number" name="carbsG"
                                   value={form.carbsG} onChange={handleChange}
                                   min={0} step="0.1" style={inputStyle} />
                        </label>
                        <label>
                            Fett (g)
                            <input type="number" name="fatG"
                                   value={form.fatG} onChange={handleChange}
                                   min={0} step="0.1" style={inputStyle} />
                        </label>
                    </div>
                </fieldset>

                {error && (
                    <p style={{ color: 'crimson', marginBottom: '1rem' }}>{error}</p>
                )}

                <button
                    type="submit"
                    disabled={submitting}
                    style={{
                        padding: '0.75rem 1.5rem',
                        fontSize: '1rem',
                        background: '#2563eb',
                        color: 'white',
                        border: 'none',
                        borderRadius: '6px',
                        cursor: submitting ? 'not-allowed' : 'pointer',
                        opacity: submitting ? 0.6 : 1
                    }}
                >
                    {submitting ? 'Speichere...' : 'Rezept speichern'}
                </button>
            </form>
        </div>
    );
}

export default RecipeForm;