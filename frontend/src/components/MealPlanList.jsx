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
                            marginBottom: '0.75rem'
                        }}
                    >
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