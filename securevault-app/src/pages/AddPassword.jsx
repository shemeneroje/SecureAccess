import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar.jsx';
import { generateStrongPassword } from '../utils/passwordUtils.jsx';

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

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };
  
  const handleGenerate = () => {
    const newPass = generateStrongPassword(20); 
    setFormData({ ...formData, password: newPass });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    addPassword(formData); // Use the function from App.js
    navigate('/'); // Go back to the dashboard
  };
  
  // Simple check for display
  const isPasswordStrong = formData.password.length >= 12;

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
              type="text" 
              name="password" 
              placeholder="Generated or Manual Password"
              value={formData.password} 
              onChange={handleChange} 
              required
            />
            <button type="button" onClick={handleGenerate} style={{ background: '#555' }}>
              Generate
            </button>
          </div>
          
          {/*  */}
          {isPasswordStrong && (
            <div className="password-strength">
              Password Strength: **Very Strong** (Length: {formData.password.length})
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
