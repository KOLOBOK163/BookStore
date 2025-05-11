// src/components/book/BookSearch.jsx
import React, { useState } from 'react';
import './BookSearch.css';

const BookSearch = ({ onSearch }) => {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSearch = () => {
    onSearch(searchTerm);
  };

  return (
    <div className="book-search">
      <input
        type="text"
        placeholder="Поиск по названию..."
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
      />
      <button onClick={handleSearch}>Найти</button>
    </div>
  );
};

export default BookSearch;