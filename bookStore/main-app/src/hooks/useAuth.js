import { useContext } from 'react';
import { AuthContext } from '../context/authContext';

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  console.log('User in useAuth (raw):', context.user); // Отладка
  console.log('roles in useAuth:', context.user?.roles); // Проверка roles
  const isAdmin = context.user?.roles?.includes('ADMIN') || false; // Изменено на 'ADMIN'
  console.log('isAdmin:', isAdmin); // Отладка

  return {
    ...context,
    isAdmin,
  };
};