import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar.jsx';
import { generateStrongPassword } from '../utils/passwordUtils.jsx';
import { Eye, EyeOff } from 'lucide-react'; 

// function to determine the stregnth of a password
const getPasswordStrength = (password) => {
    //set initial score to zero, then increase, according to the constracts below
    let score = 0;
    if (password.length > 7) score += 1;
    if (password.length > 11) score += 1;
    if (/[A-Z]/.test(password)) score += 1; // Uppercase
    if (/[a-z]/.test(password)) score += 1; // Lowercase
    if (/\d/.test(password)) score += 1; // Numbers
    if (/[^A-Za-z0-9]/.test(password)) score += 1; // Special characters
    
    if (score < 3) return { level: 'Weak', color: '#e74c3c', score: score };
    if (score < 5) return { level: 'Medium', color: '#f39c12', score: score };
    return { level: 'Strong', color: '#2ecc71', score: score };
};

function AddPassword({ passwords, addPassword, onLogout }) {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ 
    name: '', 
    username: '', 
    password: '', 
    url: '', 
    category: 'General',
    notes: '' 
  });

  // The state to control password visibility
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };
  
  const handleGenerate = () => {
    const newPass = generateStrongPassword(20); 
    setFormData({ ...formData, password: newPass });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const strength = getPasswordStrength(formData.password);

    // Password strength check prompt (asking user to make a stronger password)
    if (strength.level === 'Weak') {
        if (!window.confirm('Warning: This password is weak. Do you wish to save it anyway?')) {
            return; // Stop submission if user cancels
        }
    }

    addPassword(formData); // Use the function from App.js
    navigate('/'); // Go back to the dashboard
  };
  
  // Simple check for display
  //const isPasswordStrong = formData.password.length >= 12;

  //display for the strength using the function
  const strength = getPasswordStrength(formData.password);

  return (
    <div className="main-layout">
      <Sidebar passwordCount={passwords.length} onLogout={onLogout} />
      <div className="content-area">
        <h1>Add New Password</h1>
        <p>Enter the details for your new credential.</p>
        
        <form onSubmit={handleSubmit} style={{ maxWidth: '600px' }}>
          
          {/* Service Name */}
          <label htmlFor="name">Service Name (e.g., Google, Twitter)</label>
          <input 
            type="text" 
            name="name" 
            placeholder="Service Name"
            value={formData.name}
            onChange={handleChange} 
            required
          />
          
          {/* Username */}
          <label htmlFor="username">Username/Email</label>
          <input 
            type="text" 
            name="username" 
            placeholder="Username or Email"
            value={formData.username}
            onChange={handleChange} 
            required
          />

          {/* Password Generator/Checker */}
          <label htmlFor="password">Password *</label>
          <div className="password-input-group">
            <input 
              type={showPassword ? 'text' : 'password'} 
              name="password" 
              placeholder="Generated or Manual Password"
              value={formData.password} 
              onChange={handleChange} 
              required
            />

            {/* Eye symbol toggle for visibility */}
            <button 
              type="button" 
              onClick={() => setShowPassword(!showPassword)} 
              className="icon-btn" 
              title={showPassword ? 'Hide Password' : 'Show Password'}
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>

            <button type="button" onClick={handleGenerate} style={{ background: '#555' }}>
              Generate
            </button>
          </div>
          
          {/* Password Strength Indicator */}
          {formData.password && (
            <div className="password-strength" style={{ color: strength.color }}>
              Strength: **{strength.level}** (Score: {strength.score}/6)
            </div>
          )}
          
          {/* URL */}
          <label htmlFor="url">Website URL (Optional)</label>
          <input 
            type="url" 
            name="url" 
            placeholder="https://example.com"
            value={formData.url}
            onChange={handleChange} 
          />

          {/* Category */}
          <label htmlFor="category">Category</label>
          <input 
            type="text" 
            name="category" 
            placeholder="e.g., Social, Banking"
            value={formData.category}
            onChange={handleChange} 
          />
          
          <label htmlFor="notes">Notes (Optional)</label>
          <textarea 
            name="notes"
            rows="3"
            placeholder="Any additional details..."
            value={formData.notes}
            onChange={handleChange}
            style={{ width: '100%', padding: '10px', backgroundColor: 'var(--color-input-bg)', border: '1px solid var(--color-border)', color: 'var(--color-text-light)', borderRadius: '6px' }}
          ></textarea>

          <button type="submit" className="btn-primary">
            Save Password
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddPassword;
