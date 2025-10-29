import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login({ onLogin }) {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  
  const handleSubmit = (e) => {
    e.preventDefault();
    // In a real app, validate credentials here.
    // For this mock, we always succeed.
    
    // Call the login function passed from App.js
    onLogin(); 
    
    // Navigate to the Dashboard
    navigate('/'); 
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="logo-icon" style={{ fontSize: '2rem', marginBottom: '10px' }}>🛡️</div>
        <h2>SecureVault</h2>
        <p>Log in to manage your passwords</p>
        
        <div className="tab-buttons">
          <button className="tab-login active">Login</button>
          <button onClick={() => navigate('/register')} className="tab-register">Register</button>
        </div>
        
        <form onSubmit={handleSubmit}>
          <label htmlFor="email">Email</label>
          <input 
            type="email" 
            id="email" 
            placeholder="your@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label htmlFor="password">Master Password</label>
          <input 
            type="password" 
            id="password" 
            placeholder="********"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit" className="btn-primary login-button">
            Login
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
