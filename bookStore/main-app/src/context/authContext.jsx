import React, { createContext, useState, useEffect, useMemo } from 'react';
import { toast } from 'react-toastify';
import { getCustomerProfile } from '../services/api';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [user, setUser] = useState(JSON.parse(localStorage.getItem('user')) || null);
  const [cart, setCart] = useState(JSON.parse(localStorage.getItem('cart')) || []);

  useEffect(() => {
    localStorage.setItem('token', token || '');
    localStorage.setItem('user', JSON.stringify(user) || '');
    localStorage.setItem('cart', JSON.stringify(cart));
    console.log('User updated in AuthProvider:', user);
  }, [token, user, cart]);

  const addToCart = (book) => {
    const existingItem = cart.find((item) => item.id === book.id);
    if (existingItem) {
      setCart(
        cart.map((item) =>
          item.id === book.id ? { ...item, quantity: item.quantity + 1 } : item
        )
      );
    } else {
      setCart([...cart, { ...book, quantity: 1 }]);
    }
    toast.success(`${book.title} добавлен в корзину!`);
  };

  const refreshUser = async () => {
    if (!token) {
      console.warn('No token available to refresh user.');
      return;
    }
    try {
      const updatedUser = await getCustomerProfile();
      setUser(updatedUser);
      localStorage.setItem('user', JSON.stringify(updatedUser));
      console.log('User refreshed successfully:', updatedUser);
    } catch (err) {
      console.error('Error refreshing user:', err);
      if (err.response?.status === 401) {
        // Токен недействителен или истек
        logout();
        toast.error('Сессия истекла. Пожалуйста, войдите снова.');
      } else if (err.response?.status === 403) {
        // Ошибка доступа, но не выходим сразу
        toast.error('Ошибка доступа. Пожалуйста, попробуйте снова или войдите заново.');
      } else {
        toast.error('Ошибка при обновлении данных пользователя.');
      }
    }
  };

  const removeFromCart = (bookId) => {
    setCart(cart.filter((item) => item.id !== bookId));
    toast.info('Товар удалён из корзины.');
  };

  const updateCartQuantity = (bookId, quantity) => {
    if (quantity <= 0) {
      removeFromCart(bookId);
      return;
    }
    setCart(
      cart.map((item) =>
        item.id === bookId ? { ...item, quantity } : item
      )
    );
  };

  const clearCart = () => {
    setCart([]);
    toast.info('Корзина очищена.');
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    setCart([]);
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('cart');
    toast.success('Вы вышли из системы.');
  };

  const value = useMemo(
    () => ({
      token,
      setToken,
      user,
      setUser,
      cart,
      addToCart,
      removeFromCart,
      updateCartQuantity,
      clearCart,
      logout,
      refreshUser,
    }),
    [token, user, cart]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};