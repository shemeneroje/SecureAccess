import React from 'react';
import { Link, useLocation } from 'react-router-dom';

function Sidebar({ passwordCount, onLogout }) {
  const location = useLocation();

  return (
    <div className="sidebar">
      <div className="logo-section">🔒 SecureVault</div>
      <p>{passwordCount} passwords saved</p>
      
      <nav>
        {/* Check location.pathname to apply the 'active' class */}
        <Link 
          to="/" 
          className={location.pathname === '/' ? 'active' : ''}
        >
          My Passwords
        </Link>
        <Link 
          to="/add" 
          className={location.pathname === '/add' ? 'active' : ''}
        >
          Add New Password
        </Link>
      </nav>
      
      {/* Spacer */}
      <div style={{ flexGrow: 1 }}></div>

      <div className="encrypted-info" style={{ padding: '0 20px 20px' }}>
        <small>Data is locally encrypted before storage.</small>
      </div>
      
      {/* Logout button triggers the function from App.js */}
      <button className="logout-button" onClick={onLogout}>
        Logout
      </button>
    </div>
  );
}

export default Sidebar;
