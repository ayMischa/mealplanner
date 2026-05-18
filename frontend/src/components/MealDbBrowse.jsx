import { useState } from 'react';

function MealDbBrowse() {
    const [query, setQuery] = useState('');
    const [results, setResults] = useState([]);
    const [searching, setSearching] = useState(false);
    const [error, setError] = useState(null);
    const [importStatus, setImportStatus] = useState({}); // mealDbId -> status

    async function handleSearch(e) {
        e.preventDefault();
        if (!query.trim()) return;
        setSearching(true);
        setError(null);
        setResults([]);
        try {
            const response = await fetch(
                `http://localhost:8080/api/mealdb?q=${encodeURIComponent(query)}`
            );
            if (!response.ok) throw new Error(`HTTP ${response.status}`);
            const data = await response.json();
            setResults(data);
        } catch (err) {
            setError(err.message);
        } finally {
            setSearching(false);
        }
    }

    async function handleRandom() {
        setSearching(true);
        setError(null);
        setResults([]);
        try {
            const response = await fetch('http://localhost:8080/api/mealdb/random');
            if (!response.ok) throw new Error(`HTTP ${response.status}`);
            const data = await response.json();
            setResults([data]);
        } catch (err) {
            setError(err.message);
        } finally {
            setSearching(false);
        }
    }

    async function handleImport(mealDbId) {
        setImportStatus({ ...importStatus, [mealDbId]: 'pending' });
        try {
            const response = await fetch(
                `http://localhost:8080/api/recipes/from-mealdb/${mealDbId}`,
                { method: 'POST' }
            );
            if (!response.ok) throw new Error(`HTTP ${response.status}`);
            setImportStatus(prev => ({ ...prev, [mealDbId]: 'success' }));
        } catch (err) {
            setImportStatus(prev => ({ ...prev, [mealDbId]: 'error' }));
        }
    }

    const inputStyle = {
        flex: 1,
        padding: '0.5rem',
        fontSize: '1rem'
    };

    function getImportButtonLabel(status) {
        switch (status) {
            case 'pending': return '...';
            case 'success': return '✓ Importiert';
            case 'error': return 'Fehler – nochmal?';
            default: return 'In meine Sammlung';
        }
    }

    function getImportButtonColor(status) {
        if (status === 'success') return '#16a34a';
        if (status === 'error') return 'crimson';
        return '#2563eb';
    }

    return (
        <div>
            <h2>TheMealDB durchsuchen</h2>
            <p style={{ color: '#666', marginBottom: '1.5rem' }}>
                Such nach Rezepten und importiere sie in deine Sammlung.
            </p>

            <form onSubmit={handleSearch} style={{ display: 'flex', gap: '0.5rem', marginBottom: '1rem' }}>
                <input
                    type="text"
                    value={query}
                    onChange={e => setQuery(e.target.value)}
                    placeholder="z.B. chicken, pasta, beef..."
                    style={inputStyle}
                />
                <button
                    type="submit"
                    disabled={searching}
                    style={{
                        padding: '0.5rem 1rem',
                        background: '#2563eb',
                        color: 'white',
                        border: 'none',
                        borderRadius: '6px',
                        cursor: searching ? 'not-allowed' : 'pointer'
                    }}
                >
                    {searching ? 'Sucht...' : 'Suchen'}
                </button>
                <button
                    type="button"
                    onClick={handleRandom}
                    disabled={searching}
                    style={{
                        padding: '0.5rem 1rem',
                        background: 'transparent',
                        color: '#2563eb',
                        border: '1px solid #2563eb',
                        borderRadius: '6px',
                        cursor: searching ? 'not-allowed' : 'pointer'
                    }}
                >
                    Zufällig
                </button>
            </form>

            {error && <p style={{ color: 'crimson' }}>{error}</p>}

            {results.length === 0 && !searching && !error && (
                <p style={{ color: '#888' }}>
                    Suche etwas oder klick "Zufällig" für eine Überraschung.
                </p>
            )}

            <ul style={{ listStyle: 'none', padding: 0 }}>
                {results.map(meal => {
                    const status = importStatus[meal.idMeal];
                    return (
                        <li
                            key={meal.idMeal}
                            style={{
                                border: '1px solid #ddd',
                                borderRadius: '8px',
                                padding: '1rem',
                                marginBottom: '0.75rem',
                                display: 'flex',
                                gap: '1rem'
                            }}
                        >
                            {meal.strMealThumb && (
                                <img
                                    src={meal.strMealThumb}
                                    alt={meal.strMeal}
                                    style={{
                                        width: '120px',
                                        height: '120px',
                                        objectFit: 'cover',
                                        borderRadius: '6px',
                                        flexShrink: 0
                                    }}
                                />
                            )}
                            <div style={{ flex: 1, display: 'flex', flexDirection: 'column' }}>
                                <h3 style={{ margin: '0 0 0.5rem 0' }}>{meal.strMeal}</h3>
                                <div style={{ color: '#666', fontSize: '0.9rem', marginBottom: '0.75rem' }}>
                                    {meal.strCategory} · {meal.strArea}
                                </div>
                                <button
                                    onClick={() => handleImport(meal.idMeal)}
                                    disabled={status === 'pending' || status === 'success'}
                                    style={{
                                        alignSelf: 'flex-start',
                                        marginTop: 'auto',
                                        padding: '0.4rem 0.75rem',
                                        background: getImportButtonColor(status),
                                        color: 'white',
                                        border: 'none',
                                        borderRadius: '6px',
                                        cursor: status === 'pending' || status === 'success' ? 'default' : 'pointer',
                                        fontSize: '0.9rem'
                                    }}
                                >
                                    {getImportButtonLabel(status)}
                                </button>
                            </div>
                        </li>
                    );
                })}
            </ul>
        </div>
    );
}

export default MealDbBrowse;