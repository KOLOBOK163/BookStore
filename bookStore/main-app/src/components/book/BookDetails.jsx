import React from 'react';
import './BookDetails.css';

const BookDetails = ({ book }) => {

  return (
    <div className="book-details">
      {book.coverImage && (
        <img
          src={`http://localhost:8080${book.coverImage}`}
          alt="Обложка книги"
          style={{ width: '200px', height: 'auto', marginBottom: '20px' }}
        />
      )}
      <h2>{book.title}</h2>
      <p><strong>Автор:</strong> {book.author}</p>
      <p><strong>Цена:</strong> {book.price} ₽</p>
      <p><strong>Описание:</strong> {book.description}</p>
      <p><strong>Издатель:</strong> {book.publisher}</p>
      <p><strong>Год издания:</strong> {book.publicationYear}</p>
      <p><strong>ISBN:</strong> {book.isbn}</p>
      <p><strong>В наличии:</strong> {book.stock} шт.</p>
    </div>
  );
};

export default BookDetails;