import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Register() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    
    // Mock registration success
    console.log('User attempting to register:', formData);
    alert('Registration successful! Please login.');
    
    // Redirect to Login page
    navigate('/login'); 
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <div className="logo-icon" style={{ fontSize: '2rem', marginBottom: '10px' }}>🔐</div>
        <h2>SecureVault</h2>
        <p>Create a master account to protect your data</p>
        
        <div className="tab-buttons">
          <button onClick={() => navigate('/login')} className="tab-login">Login</button>
          <button className="tab-register active">Register</button>
        </div>
        
        <form onSubmit={handleSubmit}>
          <label htmlFor="email">Email</label>
          <input 
            type="email" 
            id="email" 
            name="email"
            placeholder="Master Account Email"
            value={formData.email}
            onChange={handleChange}
            required
          />

          <label htmlFor="password">Master Password</label>
          <input 
            type="password" 
            id="password" 
            name="password"
            placeholder="Set a Strong Master Password"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <button type="submit" className="btn-primary">
            Create Account
          </button>
        </form>
      </div>
    </div>
  );
}

export default Register;
