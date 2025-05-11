import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import './Navbar.css';

const Navbar = () => {
  const { user, logout, cart, isAdmin } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-content">
        <Link to="/" className="navbar-brand">BookLand</Link>
        <div className="navbar-links">
          <Link to="/">Главная</Link>
          <Link to="/cart">Корзина ({cart.length})</Link>
          {user ? (
            <>
              <Link to="/orders">Мои заказы</Link>
              <Link to="/profile">Профиль</Link>
              {isAdmin && <Link to="/admin">Админ-панель</Link>}
              <div className="balance-container">
                <span className="balance-label">Баланс:</span>
                <span className="balance-amount">{user.balance || 0} ₽</span>
              </div>
              <button onClick={handleLogout}>Выйти ({user.login})</button>
            </>
          ) : (
            <>
              <Link to="/login">Войти</Link>
              <Link to="/register">Регистрация</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;