import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useBooks } from '../hooks/useBooks';
import { toast } from 'react-toastify';
import './AdminDiscountForm.css'; // Подключаем стили

const AdminDiscountForm = () => {
  const { bookId } = useParams();
  const { createDiscount, updateDiscount, deleteDiscount, getDiscountsForBook, loading, error } = useBooks();
  const [discounts, setDiscounts] = useState([]);
  const [discount, setDiscount] = useState({
    discountPercentage: '',
    startDate: '',
    endDate: '',
  });
  const [editingDiscountId, setEditingDiscountId] = useState(null);

  useEffect(() => {
    let isMounted = true;

    const loadDiscounts = async () => {
      if (!bookId) {
        toast.error('ID книги не указан');
        return;
      }
      try {
        const data = await getDiscountsForBook(bookId);
        if (isMounted) {
          setDiscounts(data || []);
        }
      } catch (err) {
        console.error('Ошибка загрузки скидок:', err);
        if (isMounted) {
          toast.error('Ошибка загрузки скидок');
        }
      }
    };

    loadDiscounts();

    return () => {
      isMounted = false;
    };
  }, [bookId, getDiscountsForBook]);

  const handleChange = (e) => {
    setDiscount({ ...discount, [e.target.name]: e.target.value });
  };

  const validateDates = () => {
    if (!discount.startDate || !discount.endDate) return true;
    const start = new Date(discount.startDate);
    const end = new Date(discount.endDate);
    return start < end;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateDates()) {
      toast.error('Дата окончания не может быть раньше даты начала');
      return;
    }

    const startDate = new Date(discount.startDate);
    const endDate = new Date(discount.endDate);

    const discountData = {
      discountPercentage: parseFloat(discount.discountPercentage),
      startDate: startDate.toISOString(),
      endDate: endDate.toISOString(),
      active: true,
    };

    try {
      if (editingDiscountId) {
        await updateDiscount(editingDiscountId, discountData);
      } else {
        await createDiscount(bookId, discountData);
      }
      setDiscount({ discountPercentage: '', startDate: '', endDate: '' });
      setEditingDiscountId(null);
      const updatedDiscounts = await getDiscountsForBook(bookId);
      setDiscounts(updatedDiscounts || []);
    } catch (err) {
      console.error('Ошибка при сохранении скидки:', err);
      toast.error('Ошибка при сохранении скидки');
    }
  };

  const handleEdit = (discount) => {
    const startDate = new Date(discount.startDate);
    const endDate = new Date(discount.endDate);

    const formatDateForInput = (date) => {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day}T${hours}:${minutes}`;
    };

    setDiscount({
      discountPercentage: discount.discountPercentage,
      startDate: formatDateForInput(startDate),
      endDate: formatDateForInput(endDate),
    });
    setEditingDiscountId(discount.id);
  };

  const handleDelete = async (id) => {
    try {
      await deleteDiscount(id);
      const updatedDiscounts = await getDiscountsForBook(bookId);
      setDiscounts(updatedDiscounts || []);
    } catch (err) {
      console.error('Ошибка при удалении скидки:', err);
      toast.error('Ошибка при удалении скидки');
    }
  };

  const formatLocalDateTime = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('ru-RU', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false,
    });
  };

  if (!bookId) {
    return <p>Ошибка: ID книги не указан</p>;
  }

  if (loading) return <p>Загрузка...</p>;
  if (error) return <p>{error}</p>;

  return (
    <div className="admin-discount-container">
      <h3>Управление скидками для книги ID: {bookId}</h3>
      <form onSubmit={handleSubmit} className="admin-discount-form">
        <input
          type="number"
          name="discountPercentage"
          placeholder="Процент скидки (0-100)"
          value={discount.discountPercentage}
          onChange={handleChange}
          step="0.1"
          min="0"
          max="100"
          required
        />
        <input
          type="datetime-local"
          name="startDate"
          value={discount.startDate}
          onChange={handleChange}
          required
        />
        <input
          type="datetime-local"
          name="endDate"
          value={discount.endDate}
          onChange={handleChange}
          required
        />
        <button type="submit">{editingDiscountId ? 'Обновить' : 'Добавить'}</button>
      </form>
      <ul className="discount-list">
        {discounts.map((d) => (
          <li key={d.id} className="discount-item">
            <span>
              {d.discountPercentage}% с {formatLocalDateTime(d.startDate)} по {formatLocalDateTime(d.endDate)}
            </span>
            <div className="discount-actions">
              <button onClick={() => handleEdit(d)} className="edit">Редактировать</button>
              <button onClick={() => handleDelete(d.id)} className="delete">Удалить</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default AdminDiscountForm;