import RecipeList from './components/RecipeList';

function App() {
    return (
        <div style={{ padding: '2rem', fontFamily: 'sans-serif', maxWidth: '800px', margin: '0 auto' }}>
            <h1>Mealplanner</h1>
            <h2>Rezepte</h2>
            <RecipeList />
        </div>
    );
}

export default App;