import React, { useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login.jsx';
import Register from './pages/Register.jsx';
import Dashboard from './pages/Dashboard.jsx';
import AddPassword from './pages/AddPassword.jsx';
import ViewPassword from './pages/ViewPassword.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import { initialPasswords } from './utils/passwordUtils.jsx'; // Mock data
import Setup2FA from './pages/Setup2FA.jsx'; 

function App() {
  // State 1: Authentication status
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  
  // State 2: Application data (Passwords)
  const [passwords, setPasswords] = useState(initialPasswords);

  // Function to handle login (mocks a successful login)
  const handleLogin = () => setIsAuthenticated(true);
  
  // Function to handle logout
  const handleLogout = () => {
    setIsAuthenticated(false);
    // Note: The router handles redirection automatically after this state update
  };

  // Function to add a new password item
  const addPassword = (newPass) => {
    // Assign a unique ID using the current timestamp
    const newEntry = { ...newPass, id: Date.now() };
    setPasswords([...passwords, newEntry]);
  };

  // Function to update an existing password item
  const updatePassword = (updatedPass) => {
    setPasswords(passwords.map(p => 
      p.id === updatedPass.id ? updatedPass : p
    ));
  };
  
  // Function to delete a password item
  const deletePassword = (idToDelete) => {
    setPasswords(passwords.filter(p => p.id !== idToDelete));
  };


  return (
    <Routes>
      {/* --- Public Routes --- */}
      <Route path="/login" element={<Login onLogin={handleLogin} />} />
      <Route path="/register" element={<Register />} />
      
      {/* --- Protected Routes (Accessed only if isAuthenticated is true) --- */}
      <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}>
        {/* Main Dashboard */}
        <Route path="/" element={<Dashboard passwords={passwords} onLogout={handleLogout} />} />
        
        {/* Add New Password */}
        <Route 
          path="/add" 
          element={<AddPassword passwords={passwords} addPassword={addPassword} onLogout={handleLogout} />} 
        />
        
        {/* View/Edit Specific Password (uses URL parameter ':id') */}
        <Route 
          path="/password/:id" 
          element={
            <ViewPassword 
              passwords={passwords} 
              onLogout={handleLogout} 
              updatePassword={updatePassword} 
              deletePassword={deletePassword} 
            />
          } 
        />

        {/* 2FA Setup Route */}
          <Route 
            path="/settings/2fa" 
            element={<Setup2FA passwords={passwords} onLogout={handleLogout} />} 
          />
      </Route>

      {/* Default route: if they are authenticated, go to Dashboard. Otherwise, Login. */}
      <Route 
        path="*" 
        element={isAuthenticated ? <Navigate to="/" /> : <Navigate to="/login" />} 
      />
    </Routes>
  );
}

export default App;
