// src/components/common/BookCard.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import './BookCard.css';

const BookCard = ({ book }) => {
  const { addToCart, user } = useAuth();

  return (
    <div className="book-card">
      <Link to={`/book/${book.id}`}>
        <h3>{book.title}</h3>
        <p>{book.author}</p>
        <p>{book.price} ₽</p>
      </Link>
      {user && (
        <button onClick={() => addToCart(book)}>В корзину</button>
      )}
    </div>
  );
};

export default BookCard;