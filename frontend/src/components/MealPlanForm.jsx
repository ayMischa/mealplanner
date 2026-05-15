import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';

function MealPlanForm() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        name: '',
        description: '',
        goal: 'CUT',
        startDate: '',
        endDate: ''
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

        const payload = {
            name: form.name,
            description: form.description || null,
            goal: form.goal,
            startDate: form.startDate,
            endDate: form.endDate
        };

        try {
            const response = await fetch('http://localhost:8080/api/meal-plans', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) {
                if (response.status === 400) {
                    throw new Error('Bitte alle Pflichtfelder ausfüllen');
                }
                throw new Error(`HTTP ${response.status}`);
            }

            navigate('/meal-plans');
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
            <Link to="/meal-plans" style={{ display: 'inline-block', marginBottom: '1rem' }}>
                ← Zurück zur Liste
            </Link>

            <h2 style={{ marginTop: 0 }}>Neuer Plan</h2>

            <form onSubmit={handleSubmit}>
                <div style={{ marginBottom: '1rem' }}>
                    <label>
                        Name *
                        <input
                            type="text"
                            name="name"
                            value={form.name}
                            onChange={handleChange}
                            required
                            placeholder="z.B. Cut Phase November"
                            style={inputStyle}
                        />
                    </label>
                </div>

                <div style={{ marginBottom: '1rem' }}>
                    <label>
                        Beschreibung
                        <textarea
                            name="description"
                            value={form.description}
                            onChange={handleChange}
                            rows={3}
                            style={inputStyle}
                        />
                    </label>
                </div>

                <div style={{ marginBottom: '1rem' }}>
                    <label>
                        Ziel *
                        <select
                            name="goal"
                            value={form.goal}
                            onChange={handleChange}
                            required
                            style={inputStyle}
                        >
                            <option value="CUT">CUT (Defizit)</option>
                            <option value="BULK">BULK (Überschuss)</option>
                            <option value="MAINTAIN">MAINTAIN (Erhalt)</option>
                        </select>
                    </label>
                </div>

                <div style={{ display: 'flex', gap: '1rem', marginBottom: '1rem' }}>
                    <label style={{ flex: 1 }}>
                        Startdatum *
                        <input
                            type="date"
                            name="startDate"
                            value={form.startDate}
                            onChange={handleChange}
                            required
                            style={inputStyle}
                        />
                    </label>
                    <label style={{ flex: 1 }}>
                        Enddatum *
                        <input
                            type="date"
                            name="endDate"
                            value={form.endDate}
                            onChange={handleChange}
                            required
                            style={inputStyle}
                        />
                    </label>
                </div>

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
                    {submitting ? 'Speichere...' : 'Plan speichern'}
                </button>
            </form>
        </div>
    );
}

export default MealPlanForm;