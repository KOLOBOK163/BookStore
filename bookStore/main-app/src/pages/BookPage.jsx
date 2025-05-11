import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useBooks } from '../hooks/useBooks';
import { useAuth } from '../hooks/useAuth';
import BookDetails from '../components/book/BookDetails';
import './BookPage.css';

const BookPage = () => {
  const { id } = useParams();
  const { fetchBookById } = useBooks();
  const { addToCart, user } = useAuth();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const loadBook = async () => {
      setLoading(true);
      try {
        const data = await fetchBookById(id);
        setBook(data);
      } catch (err) {
        setError(err.response?.data?.message || 'Ошибка загрузки книги');
      } finally {
        setLoading(false);
      }
    };
    loadBook();
  }, [id]);

  const handleAddToCart = (e) => {
    e.preventDefault();
    if (!user) {
      navigate('/login');
      return;
    }
    addToCart(book);
  };

  const handleBuyNow = (e) => {
    e.preventDefault();
    if (!user) {
      navigate('/login');
      return;
    }
    addToCart(book);
    navigate('/cart');
  };

  if (loading) return <p className="book-list-loading">Загрузка...</p>;
  if (error) return <p className="book-list-error">{error}</p>;
  if (!book) return null;

  // Логика отображения цены с учётом скидки
  const discountPercentage = book.activeDiscount?.discountPercentage || book.discountPercentage || 0;
  const hasDiscount = discountPercentage > 0;
  const displayPrice = hasDiscount ? book.discountedPrice : book.price;

  return (
    <div className="book-page-container">
      <BookDetails book={book} />
      <div className="book-price">
        {hasDiscount ? (
          <>
            <span className="original-price">{book.price} ₽</span>
            <span className="discounted-price">
              {displayPrice} ₽ (-{discountPercentage}%)
            </span>
          </>
        ) : (
          <span className="regular-price">{displayPrice} ₽</span>
        )}
      </div>
      <div className="book-actions">
        <button className="buy-now-button" onClick={handleBuyNow}>
          Купить сейчас
        </button>
        <button className="add-to-cart-button" onClick={handleAddToCart}>
          Добавить в корзину
        </button>
      </div>
    </div>
  );
};

export default BookPage;