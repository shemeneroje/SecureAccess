import React, { useState } from 'react';
import Sidebar from '../components/Sidebar.jsx';
import PasswordCard from '../components/PasswordCard.jsx';

function Dashboard({ passwords, onLogout }) {
  const [searchTerm, setSearchTerm] = useState('');
  
  // Filter passwords based on search term
  const filteredPasswords = passwords.filter(pass =>
    pass.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    pass.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="main-layout">
      <Sidebar passwordCount={passwords.length} onLogout={onLogout} />
      
      <div className="content-area">
        <h1>My Passwords</h1>
        <p>Manage and organize your credentials securely</p>
        
        <input 
          type="search" 
          placeholder="Search passwords by name or username..." 
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          style={{ width: '50%', minWidth: '300px' }}
        />
        
        <div className="password-grid">
          {filteredPasswords.map(pass => (
            <PasswordCard key={pass.id} password={pass} />
          ))}
          
          {filteredPasswords.length === 0 && (
              <p style={{ marginTop: '20px', color: '#aaa' }}>No passwords found. Try adding one!</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
