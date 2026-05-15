import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';

function MealPlanList() {
    const [plans, setPlans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function fetchPlans() {
            try {
                const response = await fetch('http://localhost:8080/api/meal-plans');
                if (!response.ok) throw new Error(`HTTP ${response.status}`);
                const data = await response.json();
                setPlans(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }
        fetchPlans();
    }, []);

    async function handleDelete(id) {
        if (!confirm('Plan wirklich löschen?')) return;

        try {
            const response = await fetch(`http://localhost:8080/api/meal-plans/${id}`, {
                method: 'DELETE'
            });
            if (!response.ok && response.status !== 404) {
                throw new Error(`HTTP ${response.status}`);
            }
            setPlans(plans.filter(p => p.id !== id));
        } catch (err) {
            alert('Fehler beim Löschen: ' + err.message);
        }
    }

    let content;
    if (loading) {
        content = <p>Lädt Pläne...</p>;
    } else if (error) {
        content = <p style={{ color: 'crimson' }}>Fehler: {error}</p>;
    } else if (plans.length === 0) {
        content = <p>Noch keine Pläne. Leg einen neuen an.</p>;
    } else {
        content = (
            <ul style={{ listStyle: 'none', padding: 0 }}>
                {plans.map(plan => (
                    <li
                        key={plan.id}
                        style={{
                            border: '1px solid #ddd',
                            borderRadius: '8px',
                            padding: '1rem',
                            marginBottom: '0.75rem',
                            position: 'relative'
                        }}
                    >
                        <button
                            onClick={() => handleDelete(plan.id)}
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
                        <div style={{ paddingRight: '2rem' }}>
                            <h3 style={{ margin: '0 0 0.5rem 0' }}>{plan.name}</h3>
                            <div style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.25rem' }}>
                                Ziel: <strong>{plan.goal}</strong>
                            </div>
                            <div style={{ color: '#666', fontSize: '0.9rem' }}>
                                {plan.startDate} – {plan.endDate}
                            </div>
                            {plan.description && (
                                <div style={{ marginTop: '0.5rem', fontSize: '0.9rem' }}>
                                    {plan.description}
                                </div>
                            )}
                        </div>
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
                <h2 style={{ margin: 0 }}>Pläne</h2>
                <Link
                    to="/meal-plans/new"
                    style={{
                        padding: '0.5rem 1rem',
                        background: '#2563eb',
                        color: 'white',
                        textDecoration: 'none',
                        borderRadius: '6px',
                        fontSize: '0.9rem'
                    }}
                >
                    + Neuer Plan
                </Link>
            </div>
            {content}
        </div>
    );
}

export default MealPlanList;