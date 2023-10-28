import { Link } from 'react-router-dom';
import React from 'react';

export const NavBar: React.FC = () => (
  <div>
      <Link to="/">Home</Link>
      <Link to="/actions">Actions</Link>
      <Link to="/login">Login</Link>
  </div>
);
