// src/components/auth/Login.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { loginUser } from '../../services/api';
import { useAuth } from '../../hooks/useAuth';
import './Login.css';

const Login = () => {
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const { setToken, setUser } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async () => {
    try {
      const response = await loginUser({ login, password });
      console.log('Login response:', response); // response уже содержит {jwt, customer}
      setToken(response.jwt); // Деструктурируем jwt напрямую из response
      const newUser = { ...response.customer }; // Деструктурируем customer напрямую
      console.log('Setting user:', newUser);
      setUser(newUser);
      toast.success('Успешный вход!');
      navigate('/');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Ошибка входа');
      console.error('Login error:', err);
    }
  };
  
  return (
    <div className="login-page">
      <div className="login-container">
        <h2>Вход</h2>
        <input
          type="text"
          placeholder="Логин"
          value={login}
          onChange={(e) => setLogin(e.target.value)}
        />
        <input
          type="password"
          placeholder="Пароль"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button onClick={handleLogin}>Войти</button>
      </div>
    </div>
  );
};

export default Login;