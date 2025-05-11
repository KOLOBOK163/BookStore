import { useState, useEffect } from 'react';
import { getBooks, getBookById, searchBooks, createBook as createBookApi, updateBook as updateBookApi, deleteBook as deleteBookApi, createDiscount as createDiscountApi, updateDiscount as updateDiscountApi, deleteDiscount as deleteDiscountApi, getDiscountsForBook as getDiscountsForBookApi } from '../services/api';
import { toast } from 'react-toastify';

export const useBooks = () => {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [discountCache, setDiscountCache] = useState({});


  const handleError = (err, defaultMessage) => {
    const message = err.response?.data?.message || defaultMessage;
    setError(message);
    toast.error(message);
    return Promise.reject(err); // Возвращаем отклонённый промис
  };

  const fetchBooks = async () => {
    setLoading(true);
    try {
      const response = await getBooks();
      if (Array.isArray(response)) {
        setBooks(response);
      } else {
        setBooks([]);
        console.warn('Unexpected response format:', response);
      }
    } catch (err) {
      handleError(err, 'Ошибка загрузки книг');
    } finally {
      setLoading(false);
    }
  };

  const fetchBookById = async (id) => {
    setLoading(true);
    try {
      const response = await getBookById(id);
      console.log('Fetch Book By Id Response:', response);
      return response;
    } catch (err) {
      handleError(err, 'Ошибка загрузки книги');
    } finally {
      setLoading(false);
    }
  };

  const search = async (title) => {
    setLoading(true);
    try {
      const response = await searchBooks(title);
      console.log('Search Books Response:', response);
      if (Array.isArray(response)) {
        setBooks(response);
      } else {
        setBooks([]);
        console.warn('Unexpected response format:', response);
      }
    } catch (err) {
      handleError(err, 'Ошибка поиска');
    } finally {
      setLoading(false);
    }
  };

  const createBook = async (bookData, coverImage) => {
    setLoading(true);
    try {
      const response = await createBookApi(bookData, coverImage);
      console.log('Create Book Response:', response);
      if (response && response.id) {
        setBooks((prevBooks) => [...prevBooks, response]);
        toast.success('Книга успешно добавлена!');
        return response;
      } else {
        throw new Error('Некорректный ответ от сервера');
      }
    } catch (err) {
      handleError(err, 'Ошибка добавления книги');
    } finally {
      setLoading(false);
    }
  };

  const updateBook = async (id, bookData, coverImage) => {
    setLoading(true);
    try {
      const response = await updateBookApi(id, bookData, coverImage);
      console.log('Update Book Response:', response);
      if (response && response.id) {
        setBooks((prevBooks) =>
          prevBooks.map((book) => (book.id === response.id ? response : book))
        );
        toast.success('Книга успешно обновлена!');
        return response;
      } else {
        throw new Error('Некорректный ответ от сервера');
      }
    } catch (err) {
      handleError(err, 'Ошибка обновления книги');
    } finally {
      setLoading(false);
    }
  };

  const deleteBook = async (id) => {
    setLoading(true);
    try {
      await deleteBookApi(id);
      console.log('Delete Book Success:', id);
      setBooks((prevBooks) => prevBooks.filter((book) => book.id !== id));
      toast.success('Книга успешно удалена!');
    } catch (err) {
      handleError(err, 'Ошибка удаления книги');
    } finally {
      setLoading(false);
    }
  };

  const createDiscount = async (bookId, discountData) => {
    setLoading(true);
    try {
      const response = await createDiscountApi(bookId, discountData);
      toast.success('Скидка успешно добавлена!');
      await fetchBooks(); // Обновляем список книг после добавления скидки
      return response;
    } catch (err) {
      handleError(err, 'Ошибка добавления скидки');
    } finally {
      setLoading(false);
    }
  };

  const updateDiscount = async (id, discountData) => {
    setLoading(true);
    try {
      const response = await updateDiscountApi(id, discountData);
      toast.success('Скидка успешно обновлена!');
      await fetchBooks(); // Обновляем список книг после обновления скидки
      return response;
    } catch (err) {
      handleError(err, 'Ошибка обновления скидки');
    } finally {
      setLoading(false);
    }
  };

  const deleteDiscount = async (id) => {
    setLoading(true);
    try {
      await deleteDiscountApi(id);
      toast.success('Скидка успешно удалена!');
      await fetchBooks(); // Обновляем список книг после удаления скидки
    } catch (err) {
      handleError(err, 'Ошибка удаления скидки');
    } finally {
      setLoading(false);
    }
  };

  const getDiscountsForBook = async (bookId) => {
    if (!bookId) {
      return [];
    }
    setLoading(true);
    try {
      // Проверяем кэш
      if (discountCache[bookId]) {
        return discountCache[bookId];
      }
      const response = await getDiscountsForBookApi(bookId);
      if (response === undefined || response === null) {
        throw new Error('Получен некорректный ответ от сервера');
      }
      const discounts = Array.isArray(response) ? response : [];
      setDiscountCache((prev) => ({ ...prev, [bookId]: discounts }));
      return discounts;
    } catch (err) {
      handleError(err, 'Ошибка загрузки скидок');
      return [];
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBooks();
  }, []);

  return {
    books,
    fetchBooks,
    fetchBookById,
    search,
    createBook,
    updateBook,
    deleteBook,
    createDiscount,
    updateDiscount,
    deleteDiscount,
    getDiscountsForBook,
    loading,
    error,
  };
};