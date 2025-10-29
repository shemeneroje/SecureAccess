import React, { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Sidebar from '../components/Sidebar.jsx';

function ViewPassword({ passwords, onLogout, updatePassword, deletePassword }) {
  const { id } = useParams(); // Get the ID from the URL
  const navigate = useNavigate();
  
  // Find the password using the ID
  const initialPassword = passwords.find(p => p.id === parseInt(id));

  const [isEditing, setIsEditing] = useState(false);
  const [currentData, setCurrentData] = useState(initialPassword || {});

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
    alert('Password updated successfully!');
  };
  
  const handleDelete = () => {
    if (window.confirm(`Are you sure you want to delete the password for ${currentData.name}?`)) {
      deletePassword(currentData.id); // Delete from global state
      navigate('/'); // Redirect to the dashboard
    }
  };

  const displayField = (label, key, type = 'text') => (
    <div className="details-row">
      <span style={{ width: '120px' }}>{label}:</span>
      {isEditing ? (
        <input 
          type={type} 
          name={key} 
          value={currentData[key] || ''} 
          onChange={handleChange} 
          style={{ width: '60%' }}
        />
      ) : (
        <span style={{ color: 'white' }}>
          {type === 'password' ? '********' : currentData[key]}
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
          {displayField('Password', 'password', 'password')}
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
