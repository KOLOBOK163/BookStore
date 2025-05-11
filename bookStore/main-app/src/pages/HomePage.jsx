import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useBooks } from '../hooks/useBooks';
import './HomePage.css';
import debounce from 'lodash/debounce';

const HomePage = () => {
  const { books, fetchBooks, loading, error, search } = useBooks();
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const searchInputRef = useRef(null);
  const [currentPage, setCurrentPage] = useState(1);
  const booksPerPage = 8;

  const debouncedSearch = debounce(async (query) => {
    if (query.trim()) {
      await search(query);
    } else {
      await fetchBooks();
    }
  }, 300);

  useEffect(() => {
    fetchBooks();
  }, []);

  useEffect(() => {
    if (searchInputRef.current) {
      searchInputRef.current.focus();
    }
  }, [books]);

  const handleSearchChange = (e) => {
    const query = e.target.value;
    setSearchQuery(query);
    debouncedSearch(query);
    setCurrentPage(1);
  };

  if (loading) return <p>Загрузка...</p>;
  if (error) return <p>{error}</p>;

  const indexOfLastBook = currentPage * booksPerPage;
  const indexOfFirstBook = indexOfLastBook - booksPerPage;
  const currentBooks = books.slice(indexOfFirstBook, indexOfLastBook);
  const totalPages = Math.ceil(books.length / booksPerPage);

  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className="page-container">
      <h1>Каталог книг</h1>
      <input
        ref={searchInputRef}
        type="text"
        placeholder="Поиск по названию..."
        className="search-input"
        value={searchQuery}
        onChange={handleSearchChange}
      />
      {books.length === 0 ? (
        <p>Книги не найдены</p>
      ) : (
        <>
          <div className="book-grid">
            {currentBooks.map((book) => (
              <div
                key={book.id}
                className="book-card"
                onClick={() => navigate(`/book/${book.id}`)}
              >
                {book.coverImage && (
                  <img
                    src={`http://localhost:8080${book.coverImage}`}
                    alt="Обложка"
                    className="book-cover-image"
                  />
                )}
                <h3>{book.title}</h3>
                <p className="book-author">{book.author}</p>
                {book.activeDiscount ? (
                  <p className="book-price">
                    <span style={{ textDecoration: 'line-through', color: 'gray' }}>
                      {book.price} ₽
                    </span>{' '}
                    <span style={{ color: 'red' }}>
                      {book.discountedPrice} ₽ (-{book.activeDiscount.discountPercentage}%)
                    </span>
                  </p>
                ) : book.discountPercentage && book.discountPercentage > 0 ? (
                  <p className="book-price">
                    <span style={{ textDecoration: 'line-through', color: 'gray' }}>
                      {book.price} ₽
                    </span>{' '}
                    <span style={{ color: 'red' }}>
                      {book.discountedPrice} ₽ (-{book.discountPercentage}%)
                    </span>
                  </p>
                ) : (
                  <p className="book-price">{book.price} ₽</p>
                )}
              </div>
            ))}
          </div>
          <div className="pagination">
            <button
              onClick={() => paginate(currentPage - 1)}
              disabled={currentPage === 1}
            >
              Предыдущая
            </button>
            {Array.from({ length: totalPages }, (_, i) => i + 1).map((number) => (
              <button
                key={number}
                onClick={() => paginate(number)}
                style={{
                  fontWeight: currentPage === number ? 'bold' : 'normal',
                  backgroundColor: currentPage === number ? '#007bff' : '#fff',
                  color: currentPage === number ? '#fff' : '#000',
                }}
              >
                {number}
              </button>
            ))}
            <button
              onClick={() => paginate(currentPage + 1)}
              disabled={currentPage === totalPages}
            >
              Следующая
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default HomePage;