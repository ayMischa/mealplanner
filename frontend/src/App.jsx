import { BrowserRouter, Routes, Route, NavLink } from 'react-router-dom';
import RecipeList from './components/RecipeList';
import RecipeDetail from './components/RecipeDetail';
import RecipeForm from './components/RecipeForm';
import MealPlanList from './components/MealPlanList';
import MealPlanForm from './components/MealPlanForm';

function App() {
    const navLinkStyle = ({ isActive }) => ({
        padding: '0.5rem 1rem',
        textDecoration: 'none',
        color: isActive ? 'white' : 'inherit',
        background: isActive ? '#2563eb' : 'transparent',
        borderRadius: '6px',
        fontWeight: isActive ? 600 : 400
    });

    return (
        <BrowserRouter>
            <div style={{ padding: '2rem', fontFamily: 'sans-serif', maxWidth: '800px', margin: '0 auto' }}>
                <header style={{ marginBottom: '2rem' }}>
                    <h1 style={{ margin: '0 0 1rem 0' }}>Mealplanner</h1>
                    <nav style={{ display: 'flex', gap: '0.5rem' }}>
                        <NavLink to="/" style={navLinkStyle} end>Rezepte</NavLink>
                        <NavLink to="/meal-plans" style={navLinkStyle}>Pläne</NavLink>
                    </nav>
                </header>

                <Routes>
                    <Route path="/" element={<RecipeList />} />
                    <Route path="/recipes/new" element={<RecipeForm />} />
                    <Route path="/recipes/:id" element={<RecipeDetail />} />
                    <Route path="/meal-plans" element={<MealPlanList />} />
                    <Route path="/meal-plans/new" element={<MealPlanForm />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;