import React from 'react';
import { useNavigate } from 'react-router-dom';

function PasswordCard({ password }) {
  const navigate = useNavigate();
  
  // Clicking the card navigates to the detailed view
  const handleClick = () => {
    navigate(`/password/${password.id}`);
  };

  return (
    <div className="password-card" onClick={handleClick}>
      {/*  */}
      <h3>{password.name}</h3>
      <p>{password.username}</p>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <span className="category-tag">{password.category}</span>
        <small style={{ color: '#aaa' }}>{password.url}</small>
      </div>
    </div>
  );
}

export default PasswordCard;
