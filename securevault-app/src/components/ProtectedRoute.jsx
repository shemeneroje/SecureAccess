import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

// This component checks if the user is authenticated.
// If yes, it renders the protected component (Outlet).
// If no, it redirects them to the login page.
function ProtectedRoute({ isAuthenticated }) {
  return isAuthenticated ? <Outlet /> : <Navigate to="/login" replace />;
}

export default ProtectedRoute;
