import { BrowserRouter, Routes, Route } from 'react-router-dom';
import RecipeList from './components/RecipeList';
import RecipeDetail from './components/RecipeDetail';

function App() {
    return (
        <BrowserRouter>
            <div style={
                {
                    padding: '2rem',
                    fontFamily: 'sans-serif',
                    maxWidth: '800px',
                    margin: '0 auto'
                }
            }>
                <h1>Mealplanner</h1>
                <Routes>
                    <Route path="/" element={<RecipeList />} />
                    <Route path="/recipes/:id" element={<RecipeDetail />} />
                </Routes>
            </div>
        </BrowserRouter>
    );
}

export default App;