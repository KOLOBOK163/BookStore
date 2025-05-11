// src/pages/CartPage.jsx
import React from 'react';
import Cart from '../components/cart/Cart';
import { useAuth } from '../hooks/useAuth';
import { Navigate } from 'react-router-dom';

const CartPage = () => {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" />;
  }

  return <Cart />;
};

export default CartPage;