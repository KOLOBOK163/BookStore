import { useState, useCallback } from 'react';
import { getOrders, createOrder } from '../services/api';

export const useOrders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchOrders = useCallback(async () => {
    setLoading(true);
    try {
      const response = await getOrders();
      const data = response.data || response;
      setOrders(Array.isArray(data) ? data : []);
    } catch (err) {
      const errorMessage = err.response?.data?.message || err.response?.statusText || err.message || 'Неизвестная ошибка';
      setError(errorMessage);
      console.error('Fetch orders error:', {
        status: err.response?.status,
        data: err.response?.data,
        message: errorMessage,
      });
    } finally {
      setLoading(false);
    }
  }, []);

  const placeOrder = async (orderData) => {
    setLoading(true);
    try {
      const response = await createOrder(orderData);
      const newOrder = response.data || response;
      if (newOrder && newOrder.id) {
        await fetchOrders(); // Синхронизируем состояние с сервером
        return newOrder;
      } else {
        throw new Error('Некорректный ответ от сервера');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Ошибка создания заказа');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { orders, setOrders, fetchOrders, placeOrder, loading, error };
};