import { useState, useEffect } from 'react';
import { getAllWarehouses, createWarehouse, updateWarehouse, deleteWarehouse } from '../services/api';
import { toast } from 'react-toastify';

export const useWarehouses = () => {
  const [warehouses, setWarehouses] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleError = (err, defaultMessage) => {
    const message = err.response?.data?.message || defaultMessage;
    setError(message);
    toast.error(message);
    throw err;
  };

  const fetchWarehouses = async () => {
    setLoading(true);
    try {
      const response = await getAllWarehouses();
      setWarehouses(Array.isArray(response) ? response : []);
    } catch (err) {
      handleError(err, 'Ошибка загрузки складов');
    } finally {
      setLoading(false);
    }
  };

  const createNewWarehouse = async (warehouseData) => {
    setLoading(true);
    try {
      const response = await createWarehouse(warehouseData);
      setWarehouses((prev) => [...prev, response]);
      toast.success('Склад успешно создан!');
      return response;
    } catch (err) {
      handleError(err, 'Ошибка создания склада');
    } finally {
      setLoading(false);
    }
  };

  const updateExistingWarehouse = async (id, warehouseData) => {
    setLoading(true);
    try {
      const response = await updateWarehouse(id, warehouseData);
      setWarehouses((prev) =>
        prev.map((warehouse) => (warehouse.id === id ? response : warehouse))
      );
      toast.success('Склад успешно обновлён!');
      return response;
    } catch (err) {
      handleError(err, 'Ошибка обновления склада');
    } finally {
      setLoading(false);
    }
  };

  const deleteExistingWarehouse = async (id) => {
    setLoading(true);
    try {
      await deleteWarehouse(id);
      setWarehouses((prev) => prev.filter((warehouse) => warehouse.id !== id));
      toast.success('Склад успешно удалён!');
    } catch (err) {
      handleError(err, 'Ошибка удаления склада');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchWarehouses();
  }, []);

  return { warehouses, fetchWarehouses, createNewWarehouse, updateExistingWarehouse, deleteExistingWarehouse, loading, error };
};