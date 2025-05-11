// src/components/auth/Register.jsx
import React, { useState } from 'react';
import { registerUser } from '../../services/api';
import { FaUser, FaLock, FaEnvelope, FaUserTag, FaSpinner } from 'react-icons/fa';
import './Register.css';

const Register = () => {
  const [formData, setFormData] = useState({
    login: '',
    password: '',
    email: '',
    fullName: ''
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');

  const validateForm = () => {
    const newErrors = {};
    if (formData.login.length < 8) {
      newErrors.login = 'Логин должен содержать минимум 8 символов';
    }
    if (formData.password.length < 8) {
      newErrors.password = 'Пароль должен содержать минимум 8 символов';
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Введите корректный email';
    }
    if (!formData.fullName.trim()) {
      newErrors.fullName = 'Полное имя не может быть пустым';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setSuccessMessage('');
  
    if (!validateForm()) {
      return;
    }
  
    setLoading(true);
    try {
      const response = await registerUser(formData);
      console.log('Регистрация успешна:', response); // response уже содержит {jwt, customer}
      const { jwt } = response; // Деструктурируем напрямую из response
      if (jwt) {
        localStorage.setItem('token', jwt);
        setSuccessMessage('Регистрация прошла успешно! Вы будете перенаправлены...');
        setTimeout(() => {
          window.location.href = '/login';
        }, 2000);
      }
    } catch (err) {
      setErrors({ server: err.response?.data?.message || 'Ошибка регистрации' });
      console.error('Ошибка:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  return (
    <div className="register-page">
      <div className="register-container">
        <h2>Регистрация</h2>

        {successMessage && (
          <div className="success-message">{successMessage}</div>
        )}

        {errors.server && (
          <div className="error-message">{errors.server}</div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Логин</label>
            <div className={`input-wrapper ${errors.login ? 'error' : ''}`}>
              <FaUser />
              <input
                type="text"
                name="login"
                value={formData.login}
                onChange={handleChange}
                placeholder="Введите логин"
                required
              />
            </div>
            {errors.login && <p className="error-text">{errors.login}</p>}
          </div>

          <div className="form-group">
            <label>Пароль</label>
            <div className={`input-wrapper ${errors.password ? 'error' : ''}`}>
              <FaLock />
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Введите пароль"
                required
              />
            </div>
            {errors.password && <p className="error-text">{errors.password}</p>}
          </div>

          <div className="form-group">
            <label>Email</label>
            <div className={`input-wrapper ${errors.email ? 'error' : ''}`}>
              <FaEnvelope />
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Введите email"
                required
              />
            </div>
            {errors.email && <p className="error-text">{errors.email}</p>}
          </div>

          <div className="form-group">
            <label>Полное имя</label>
            <div className={`input-wrapper ${errors.fullName ? 'error' : ''}`}>
              <FaUserTag />
              <input
                type="text"
                name="fullName"
                value={formData.fullName}
                onChange={handleChange}
                placeholder="Введите полное имя"
                required
              />
            </div>
            {errors.fullName && <p className="error-text">{errors.fullName}</p>}
          </div>

          <button
            type="submit"
            disabled={loading}
            className="submit-button"
          >
            {loading ? (
              <>
                <FaSpinner className="animate-spin" />
                <span>Загрузка...</span>
              </>
            ) : (
              <span>Зарегистрироваться</span>
            )}
          </button>
        </form>

        <p className="login-link">
          Уже есть аккаунт?{' '}
          <a href="/login">Войдите</a>
        </p>
      </div>
    </div>
  );
};

export default Register;