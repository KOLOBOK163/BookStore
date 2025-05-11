// src/components/book/BookList.jsx
import React from 'react';
import BookCard from '../common/BookCard';
import './BookList.css';

const BookList = ({ books, loading, error }) => {
  if (loading) return <p className="book-list-loading">Загрузка...</p>;
  if (error) return <p className="book-list-error">{error}</p>;

  return (
    <div className="book-list">
      {books.map((book) => (
        <BookCard key={book.id} book={book} />
      ))}
    </div>
  );
};

export default BookList;