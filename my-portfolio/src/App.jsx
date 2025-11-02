// src/App.jsx
import './App.css'; // Import the global styles
import Experience from './components/Experience';
import Footer from './components/Footer';
import Header from './components/Header';
import Hero from './components/Hero';
import Main from './components/Main';
import Overview from './components/Overview';
import Projects from './components/Projects';

const App = () => {
  return (
    <div className="app-container">
    <Header/>
    <Hero/>
    <Overview/>
      <Main/>
      <Experience/>
      <Projects/>
      <Footer/>
    </div>
  );
};

export default App;