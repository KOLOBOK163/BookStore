import { useState, useEffect } from 'react';
import { getAddresses, addAddress } from '../services/api';

export const useAddresses = () => {
  const [addresses, setAddresses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchAddresses = async () => {
    setLoading(true);
    try {
      const response = await getAddresses();
      const data = response.data || response;
      setAddresses(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.response?.data?.message || 'Ошибка загрузки адресов');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const createAddress = async (addressData) => {
    setLoading(true);
    try {
      const response = await addAddress(addressData);
      const newAddress = response.data || response;
      if (newAddress && newAddress.id) {
        await fetchAddresses(); // Синхронизируем состояние с сервером
        return newAddress;
      } else {
        throw new Error('Некорректный ответ от сервера');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Ошибка создания адреса');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAddresses();
  }, []);

  return { addresses, fetchAddresses, createAddress, loading, error };
};