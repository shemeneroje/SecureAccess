import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar.jsx';
import { Eye, EyeOff } from 'lucide-react';

function ViewPassword({ passwords, onLogout, updatePassword, deletePassword }) {
  const { id } = useParams(); // Get the ID from the URL
  const navigate = useNavigate();
  
  // Find the password using the ID
  const initialPassword = passwords.find(p => p.id === parseInt(id));

  const [isEditing, setIsEditing] = useState(false);
  const [currentData, setCurrentData] = useState(initialPassword || {});

  // Code to control password visibility
  const [showPassword, setShowPassword] = useState(false);

  // Handle case where password is not found (shouldn't happen with ProtectedRoute)
  if (!initialPassword) {
    return <p className="content-area">Error: Password not found!</p>;
  }

  const handleChange = (e) => {
    setCurrentData({ ...currentData, [e.target.name]: e.target.value });
  };

  const handleUpdate = (e) => {
    e.preventDefault();
    updatePassword(currentData); // Update the global state in App.jsx
    setIsEditing(false);
    setShowPassword(false); //To hide the password after saving
    alert('Password updated successfully!');
  };
  
  const handleDelete = () => {
    if (window.confirm(`Are you sure you want to delete the password for ${currentData.name}?`)) {
      deletePassword(currentData.id); // Delete from global state
      navigate('/'); // Redirect to the dashboard
    }
  };

  const displayField = (label, key, isPassword = false) => (
    <div className="details-row">
      <span style={{ width: '120px' }}>{label}:</span>
      
      {isEditing ? (
        // Editing Mode: Input field with toggle button
        <div style={{ flexGrow: 1, display: 'flex' }}>
          <input 
            type={isPassword && !showPassword ? 'password' : 'text'} 
            name={key} 
            value={currentData[key] || ''} 
            onChange={handleChange} 
            style={{ flexGrow: 1 }}
          />
          {isPassword && (
            <button 
              type="button" 
              onClick={() => setShowPassword(!showPassword)} 
              className="icon-btn" 
              title={showPassword ? 'Hide Password' : 'Show Password'}
              style={{ padding: '0 10px', height: '40px', background: 'none' }}
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>
          )}
        </div>
      ) : (
        // Viewing Mode: Display value with toggle button for passwords
        <span style={{ color: 'white', display: 'flex', alignItems: 'center', gap: '10px' }}>
          {/* Display logic: show asterisks if it's a password and is hiddden */}
          {isPassword && !showPassword ? '********' : currentData[key]}
          
          {isPassword && (
            <button 
              type="button" 
              onClick={() => setShowPassword(!showPassword)} 
              className="icon-btn" 
              title={showPassword ? 'Hide Password' : 'Show Password'}
              style={{ padding: 0, background: 'none' }}
            >
              {showPassword ? <EyeOff size={20} /> : <Eye size={20} />}
            </button>
          )}
        </span>
      )}
    </div>
  );

  return (
    <div className="main-layout">
      <Sidebar passwordCount={passwords.length} onLogout={onLogout} />
      <div className="content-area">
        <button 
          onClick={() => navigate('/')} 
          style={{ background: 'none', color: '#ccc', padding: 0, marginBottom: '20px' }}
        >
          ← Back to Dashboard
        </button>
        
        <h1>{currentData.name} Details</h1>
        
        <div style={{ display: 'flex', gap: '10px', marginTop: '20px' }}>
          <button onClick={() => setIsEditing(!isEditing)} style={{ background: isEditing ? '#f39c12' : '#27ae60' }}>
            {isEditing ? 'Cancel Edit' : 'Edit Details'}
          </button>
          <button onClick={handleDelete} style={{ background: '#e74c3c' }}>
            Delete Password
          </button>
        </div>
        
        <form onSubmit={handleUpdate} className="details-box">
          {displayField('Service Name', 'name')}
          {displayField('Username/Email', 'username')}
          {displayField('Password', 'password', true)} {/* Adding Pass true for isPassword */}
          {displayField('URL', 'url')}
          {displayField('Category', 'category')}
          
          <label htmlFor="notes">Notes:</label>
          {isEditing ? (
            <textarea 
              name="notes"
              rows="4"
              value={currentData.notes || ''}
              onChange={handleChange}
              style={{ width: '100%', padding: '10px', backgroundColor: 'var(--color-input-bg)', border: '1px solid var(--color-border)', color: 'var(--color-text-light)', borderRadius: '6px' }}
            ></textarea>
          ) : (
            <p style={{ backgroundColor: '#20203a', padding: '10px', borderRadius: '4px', color: '#ccc' }}>
              {currentData.notes || 'No additional notes.'}
            </p>
          )}

          {isEditing && (
            <button type="submit" className="btn-primary" style={{ marginTop: '30px' }}>
              Save Changes
            </button>
          )}
        </form>
      </div>
    </div>
  );
}

export default ViewPassword;
