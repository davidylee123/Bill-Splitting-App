import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import BillPage from './pages/BillPage';
import './App.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path=":path" element={<BillPage />} />
        {/* Add more routes */}
      </Routes>
    </Router>
  );
}

export default App;