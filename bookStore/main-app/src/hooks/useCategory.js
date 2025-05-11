import { useState, useEffect } from 'react';
import { getCategories, createCategory } from '../services/api';
import { toast } from 'react-toastify';

export const useCategory = () => {
  const [categories, setCategories] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleError = (err, defaultMessage) => {
    const message = err.response?.data?.message || defaultMessage || 'Произошла ошибка';
    setError(message);
    toast.error(message);
    setLoading(false);
    return Promise.reject(err);
  };

  const fetchCategories = async () => {
    setLoading(true);
    try {
      const response = await getCategories();
      console.log('Fetch Categories Response:', response);
      if (Array.isArray(response)) {
        setCategories(response);
      } else {
        setCategories([]);
        console.warn('Unexpected response format:', response);
      }
    } catch (err) {
      handleError(err, 'Ошибка загрузки категорий');
    } finally {
      setLoading(false);
    }
  };

  const createCategory = async (categoryData) => {
    setLoading(true);
    try {
      const response = await createCategory(categoryData);
      console.log('Create Category Response:', response);
      if (response && typeof response === 'object' && response.id) {
        setCategories((prevCategories) => [...prevCategories, response]);
        toast.success('Категория успешно добавлена!');
        return response;
      } else {
        throw new Error('Некорректный ответ от сервера: response is undefined or missing id');
      }
    } catch (err) {
      handleError(err, 'Ошибка добавления категории');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []); // Пустой массив зависимостей

  return { categories, fetchCategories, createCategory, loading, error };
};