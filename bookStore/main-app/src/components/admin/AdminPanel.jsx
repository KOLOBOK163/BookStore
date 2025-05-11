import React, { useState, useEffect } from 'react';
import { useBooks } from '../../hooks/useBooks';
import { useWarehouses } from '../../hooks/useWarehouses';
import { distributeBook, getBooksInWarehouse, updateBookStock, deleteBookFromWarehouse } from '../../services/api';
import { toast } from 'react-toastify';
import { Link } from 'react-router-dom';
import './AdminPanel.css';

const AdminPanel = () => {
  const { books, fetchBooks, loading: booksLoading, error: booksError, createBook, updateBook, deleteBook } = useBooks();
  const { warehouses, fetchWarehouses, createNewWarehouse, updateExistingWarehouse, deleteExistingWarehouse, loading: warehousesLoading, error: warehousesError } = useWarehouses();

  const [newBook, setNewBook] = useState({
    title: '',
    author: '',
    price: '',
    description: '',
    publisher: '',
    publicationYear: '',
    isbn: '',
    stock: '',
    category: '',
  });

  const [newWarehouse, setNewWarehouse] = useState({
    name: '',
    city: '',
    street: '',
    houseNumber: ''
  });

  const [distribution, setDistribution] = useState({
    bookId: '',
    warehouseId: '',
    stock: ''
  });

  const [selectedWarehouseId, setSelectedWarehouseId] = useState('');
  const [warehouseBooks, setWarehouseBooks] = useState([]);
  const [warehouseBooksLoading, setWarehouseBooksLoading] = useState(false);
  const [warehouseBooksError, setWarehouseBooksError] = useState(null);

  const [coverImage, setCoverImage] = useState(null);
  const [formErrors, setFormErrors] = useState({});
  const [warehouseFormErrors, setWarehouseFormErrors] = useState({});
  const [editingBookId, setEditingBookId] = useState(null);
  const [editingWarehouseId, setEditingWarehouseId] = useState(null);
  const [currentBookPage, setCurrentBookPage] = useState(1);
  const [currentWarehousePage, setCurrentWarehousePage] = useState(1);
  const [editingBookStock, setEditingBookStock] = useState(null);
  const itemsPerPage = 5;

  useEffect(() => {
    fetchBooks();
    fetchWarehouses();
  }, []);

  const fetchWarehouseBooks = async (warehouseId) => {
    if (!warehouseId) {
      setWarehouseBooks([]);
      return;
    }
    setWarehouseBooksLoading(true);
    try {
      const response = await getBooksInWarehouse(warehouseId);
      setWarehouseBooks(Array.isArray(response) ? response : []);
    } catch (err) {
      const message = err.response?.data?.message || 'Ошибка загрузки книг на складе';
      setWarehouseBooksError(message);
      toast.error(message);
    } finally {
      setWarehouseBooksLoading(false);
    }
  };

  const handleWarehouseSelectionChange = (e) => {
    const warehouseId = e.target.value;
    setSelectedWarehouseId(warehouseId);
    fetchWarehouseBooks(warehouseId);
  };

  const validateBookForm = () => {
    const errors = {};
    if (!newBook.title.trim()) errors.title = 'Название не может быть пустым';
    if (!newBook.author.trim()) errors.author = 'Автор не может быть пустым';
    if (!newBook.price || parseFloat(newBook.price) <= 0) errors.price = 'Цена должна быть больше 0';
    if (!newBook.publicationYear || parseInt(newBook.publicationYear) <= 0) errors.publicationYear = 'Год издания должен быть больше 0';
    if (newBook.isbn && newBook.isbn.length !== 13) errors.isbn = 'ISBN должен содержать 13 символов';
    if (!newBook.stock || parseInt(newBook.stock) < 0) errors.stock = 'Количество на складе не может быть меньше 0';
    if (!newBook.category.trim()) errors.category = 'Категория не может быть пустой';
    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const validateWarehouseForm = () => {
    const errors = {};
    if (!newWarehouse.name.trim()) errors.name = 'Название склада не может быть пустым';
    if (!newWarehouse.city.trim()) errors.city = 'Город не может быть пустым';
    if (!newWarehouse.street.trim()) errors.street = 'Улица не может быть пустой';
    if (!newWarehouse.houseNumber.trim()) errors.houseNumber = 'Номер дома не может быть пустым';
    setWarehouseFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleBookInputChange = (e) => {
    setNewBook({ ...newBook, [e.target.name]: e.target.value });
    setFormErrors({ ...formErrors, [e.target.name]: '' });
  };

  const handleWarehouseInputChange = (e) => {
    setNewWarehouse({ ...newWarehouse, [e.target.name]: e.target.value });
    setWarehouseFormErrors({ ...warehouseFormErrors, [e.target.name]: '' });
  };

  const handleDistributionChange = (e) => {
    setDistribution({ ...distribution, [e.target.name]: e.target.value });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && file.type.startsWith('image/') && file.size < 5 * 1024 * 1024) {
      setCoverImage(file);
    } else {
      setCoverImage(null);
      alert('Пожалуйста, выберите изображение размером менее 5MB');
    }
  };

  const handleAddOrUpdateBook = async (e) => {
    e.preventDefault();
    if (!validateBookForm()) return;

    const bookData = {
      ...newBook,
      price: parseFloat(newBook.price),
      publicationYear: parseInt(newBook.publicationYear),
      stock: parseInt(newBook.stock),
    };

    try {
      if (editingBookId) {
        await updateBook(editingBookId, bookData, coverImage);
        setEditingBookId(null);
      } else {
        await createBook(bookData, coverImage);
      }
      setNewBook({
        title: '',
        author: '',
        price: '',
        description: '',
        publisher: '',
        publicationYear: '',
        isbn: '',
        stock: '',
        category: '',
      });
      setCoverImage(null);
      setCurrentBookPage(1);
      await fetchBooks();
    } catch (err) {
      console.error('Ошибка добавления/обновления книги:', err);
    }
  };

  const handleAddOrUpdateWarehouse = async (e) => {
    e.preventDefault();
    if (!validateWarehouseForm()) return;

    try {
      if (editingWarehouseId) {
        await updateExistingWarehouse(editingWarehouseId, newWarehouse);
        setEditingWarehouseId(null);
      } else {
        await createNewWarehouse(newWarehouse);
      }
      setNewWarehouse({ name: '', city: '', street: '', houseNumber: '' });
      setCurrentWarehousePage(1);
      await fetchWarehouses();
    } catch (err) {
      console.error('Ошибка добавления/обновления склада:', err);
    }
  };

  const handleDistributeBook = async (e) => {
    e.preventDefault();
    if (!distribution.bookId || !distribution.warehouseId || !distribution.stock || parseInt(distribution.stock) <= 0) {
      toast.error('Пожалуйста, выберите книгу, склад и укажите количество больше 0');
      return;
    }

    try {
      await distributeBook(distribution.bookId, distribution.warehouseId, parseInt(distribution.stock));
      toast.success('Книга успешно распределена на склад!');
      setDistribution({ bookId: '', warehouseId: '', stock: '' });
      await fetchBooks();
      await fetchWarehouses();
      if (selectedWarehouseId === distribution.warehouseId) {
        await fetchWarehouseBooks(selectedWarehouseId);
      }
    } catch (err) {
      toast.error('Ошибка распределения книги: ' + (err.response?.data?.message || 'Неизвестная ошибка'));
      console.error(err);
    }
  };

  const handleEditBook = (book) => {
    setNewBook({
      title: book.title,
      author: book.author,
      price: book.price.toString(),
      description: book.description || '',
      publisher: book.publisher || '',
      publicationYear: book.publicationYear.toString(),
      isbn: book.isbn || '',
      stock: book.stock.toString(),
      category: book.category,
    });
    setEditingBookId(book.id);
    setCoverImage(null);
  };

  const handleEditWarehouse = (warehouse) => {
    setNewWarehouse({
      name: warehouse.name,
      city: warehouse.city,
      street: warehouse.street,
      houseNumber: warehouse.houseNumber,
    });
    setEditingWarehouseId(warehouse.id);
  };

  const handleDeleteBook = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить эту книгу?')) {
      try {
        await deleteBook(id);
        setCurrentBookPage(1);
        await fetchBooks();
        if (selectedWarehouseId) {
          await fetchWarehouseBooks(selectedWarehouseId);
        }
      } catch (err) {
        console.error('Ошибка удаления книги:', err);
      }
    }
  };

  const handleDeleteWarehouse = async (id) => {
    if (window.confirm('Вы уверены, что хотите удалить этот склад?')) {
      try {
        await deleteExistingWarehouse(id);
        setCurrentWarehousePage(1);
        setSelectedWarehouseId('');
        setWarehouseBooks([]);
        await fetchWarehouses();
      } catch (err) {
        console.error('Ошибка удаления склада:', err);
      }
    }
  };

  const handleEditBookStock = (book) => {
    setEditingBookStock({ bookId: book.bookId, warehouseId: selectedWarehouseId, stock: book.stock });
  };

  const handleUpdateBookStock = async (e) => {
    e.preventDefault();
    if (!editingBookStock || !editingBookStock.stock || parseInt(editingBookStock.stock) < 0) {
      toast.error('Количество не может быть меньше 0');
      return;
    }

    try {
      const bookWarehouseDTO = { stock: parseInt(editingBookStock.stock) };
      await updateBookStock(editingBookStock.warehouseId, editingBookStock.bookId, bookWarehouseDTO);
      toast.success('Количество обновлено!');
      setEditingBookStock(null);
      await fetchWarehouseBooks(selectedWarehouseId);
    } catch (err) {
      toast.error('Ошибка обновления количества: ' + (err.response?.data?.message || 'Неизвестная ошибка'));
      console.error(err);
    }
  };

  const handleDeleteBookFromWarehouse = async (bookId) => {
    if (window.confirm('Вы уверены, что хотите удалить эту книгу со склада?')) {
      try {
        await deleteBookFromWarehouse(selectedWarehouseId, bookId);
        toast.success('Книга удалена со склада!');
        await fetchWarehouseBooks(selectedWarehouseId);
      } catch (err) {
        toast.error('Ошибка удаления книги со склада: ' + (err.response?.data?.message || 'Неизвестная ошибка'));
        console.error(err);
      }
    }
  };

  const handleCancelEditBook = () => {
    setNewBook({
      title: '',
      author: '',
      price: '',
      description: '',
      publisher: '',
      publicationYear: '',
      isbn: '',
      stock: '',
      category: '',
    });
    setCoverImage(null);
    setEditingBookId(null);
    setFormErrors({});
  };

  const handleCancelEditWarehouse = () => {
    setNewWarehouse({ name: '', city: '', street: '', houseNumber: '' });
    setEditingWarehouseId(null);
    setWarehouseFormErrors({});
  };

  if (booksLoading || warehousesLoading) return <p className="admin-loading">Загрузка...</p>;
  if (booksError) return <p className="admin-error">{booksError}</p>;
  if (warehousesError) return <p className="admin-error">{warehousesError}</p>;

  const indexOfLastBook = currentBookPage * itemsPerPage;
  const indexOfFirstBook = indexOfLastBook - itemsPerPage;
  const currentBooks = books.slice(indexOfFirstBook, indexOfLastBook);
  const totalBookPages = Math.ceil(books.length / itemsPerPage);

  const indexOfLastWarehouse = currentWarehousePage * itemsPerPage;
  const indexOfFirstWarehouse = indexOfLastWarehouse - itemsPerPage;
  const currentWarehouses = warehouses.slice(indexOfFirstWarehouse, indexOfLastWarehouse);
  const totalWarehousePages = Math.ceil(warehouses.length / itemsPerPage);

  const paginateBooks = (pageNumber) => setCurrentBookPage(pageNumber);
  const paginateWarehouses = (pageNumber) => setCurrentWarehousePage(pageNumber);

  return (
    <div className="admin-container">
      <h2>Панель управления</h2>

      <h3>Управление книгами</h3>
      <form onSubmit={handleAddOrUpdateBook} className="admin-form" encType="multipart/form-data">
        <div>
          <input type="text" name="title" placeholder="Название" value={newBook.title} onChange={handleBookInputChange} required />
          {formErrors.title && <p className="admin-error">{formErrors.title}</p>}
        </div>
        <div>
          <input type="text" name="author" placeholder="Автор" value={newBook.author} onChange={handleBookInputChange} required />
          {formErrors.author && <p className="admin-error">{formErrors.author}</p>}
        </div>
        <div>
          <input type="number" step="0.01" name="price" placeholder="Цена" value={newBook.price} onChange={handleBookInputChange} required />
          {formErrors.price && <p className="admin-error">{formErrors.price}</p>}
        </div>
        <div>
          <input type="text" name="description" placeholder="Описание" value={newBook.description} onChange={handleBookInputChange} />
        </div>
        <div>
          <input type="text" name="publisher" placeholder="Издатель" value={newBook.publisher} onChange={handleBookInputChange} />
        </div>
        <div>
          <input type="number" name="publicationYear" placeholder="Год издания" value={newBook.publicationYear} onChange={handleBookInputChange} required />
          {formErrors.publicationYear && <p className="admin-error">{formErrors.publicationYear}</p>}
        </div>
        <div>
          <input type="text" name="isbn" placeholder="ISBN (13 символов)" value={newBook.isbn} onChange={handleBookInputChange} />
          {formErrors.isbn && <p className="admin-error">{formErrors.isbn}</p>}
        </div>
        <div>
          <input type="number" name="stock" placeholder="Количество на складе" value={newBook.stock} onChange={handleBookInputChange} required />
          {formErrors.stock && <p className="admin-error">{formErrors.stock}</p>}
        </div>
        <div>
          <input type="text" name="category" placeholder="Категория" value={newBook.category} onChange={handleBookInputChange} required />
          {formErrors.category && <p className="admin-error">{formErrors.category}</p>}
        </div>
        <div>
          <input type="file" name="coverImage" accept="image/*" onChange={handleFileChange} />
        </div>
        <div className="form-buttons">
          <button type="submit">{editingBookId ? 'Обновить книгу' : 'Добавить книгу'}</button>
          {editingBookId && <button type="button" onClick={handleCancelEditBook} className="cancel-button">Отмена</button>}
        </div>
      </form>

      <div className="admin-book-list">
      <h4>Список книг</h4>
      {currentBooks.map((book) => (
        <div key={book.id} className="admin-book-item">
          <p>{book.title} - {book.author} ({book.price} ₽) - {book.category} - Количество: {book.stock}</p>
          <div className="admin-book-actions">
            <button onClick={() => handleEditBook(book)} className="edit-button">Редактировать</button>
            <button onClick={() => handleDeleteBook(book.id)} className="delete-button">Удалить</button>
            <Link to={`/admin/books/${book.id}/discounts`} className="manage-discounts-button">
              Управление скидками
            </Link>
          </div>
        </div>
      ))}
        <div className="pagination">
          <button onClick={() => paginateBooks(currentBookPage - 1)} disabled={currentBookPage === 1}>Предыдущая</button>
          {Array.from({ length: totalBookPages }, (_, i) => i + 1).map((number) => (
            <button key={number} onClick={() => paginateBooks(number)} className={currentBookPage === number ? 'active' : ''}>{number}</button>
          ))}
          <button onClick={() => paginateBooks(currentBookPage + 1)} disabled={currentBookPage === totalBookPages}>Следующая</button>
        </div>
      </div>

      <h3>Управление складами</h3>
      <form onSubmit={handleAddOrUpdateWarehouse} className="admin-form">
        <div>
          <input type="text" name="name" placeholder="Название склада" value={newWarehouse.name} onChange={handleWarehouseInputChange} required />
          {warehouseFormErrors.name && <p className="admin-error">{warehouseFormErrors.name}</p>}
        </div>
        <div>
          <input type="text" name="city" placeholder="Город" value={newWarehouse.city} onChange={handleWarehouseInputChange} required />
          {warehouseFormErrors.city && <p className="admin-error">{warehouseFormErrors.city}</p>}
        </div>
        <div>
          <input type="text" name="street" placeholder="Улица" value={newWarehouse.street} onChange={handleWarehouseInputChange} required />
          {warehouseFormErrors.street && <p className="admin-error">{warehouseFormErrors.street}</p>}
        </div>
        <div>
          <input type="text" name="houseNumber" placeholder="Номер дома" value={newWarehouse.houseNumber} onChange={handleWarehouseInputChange} required />
          {warehouseFormErrors.houseNumber && <p className="admin-error">{warehouseFormErrors.houseNumber}</p>}
        </div>
        <div className="form-buttons">
          <button type="submit">{editingWarehouseId ? 'Обновить склад' : 'Добавить склад'}</button>
          {editingWarehouseId && <button type="button" onClick={handleCancelEditWarehouse} className="cancel-button">Отмена</button>}
        </div>
      </form>

      <div className="admin-warehouse-list">
        <h4>Список складов</h4>
        {currentWarehouses.map((warehouse) => (
          <div key={warehouse.id} className="admin-warehouse-item">
            <p>{warehouse.name} - {warehouse.city}, {warehouse.street}, {warehouse.houseNumber}</p>
            <div className="admin-warehouse-actions">
              <button onClick={() => handleEditWarehouse(warehouse)} className="edit-button">Редактировать</button>
              <button onClick={() => handleDeleteWarehouse(warehouse.id)} className="delete-button">Удалить</button>
            </div>
          </div>
        ))}
        <div className="pagination">
          <button onClick={() => paginateWarehouses(currentWarehousePage - 1)} disabled={currentWarehousePage === 1}>Предыдущая</button>
          {Array.from({ length: totalWarehousePages }, (_, i) => i + 1).map((number) => (
            <button key={number} onClick={() => paginateWarehouses(number)} className={currentWarehousePage === number ? 'active' : ''}>{number}</button>
          ))}
          <button onClick={() => paginateWarehouses(currentWarehousePage + 1)} disabled={currentWarehousePage === totalWarehousePages}>Следующая</button>
        </div>
      </div>

      <h3>Просмотр книг на складе</h3>
      <div className="admin-form">
        <div>
          <select value={selectedWarehouseId} onChange={handleWarehouseSelectionChange}>
            <option value="">Выберите склад</option>
            {warehouses.map((warehouse) => (
              <option key={warehouse.id} value={warehouse.id}>
                {warehouse.name} ({warehouse.city}, {warehouse.street}, {warehouse.houseNumber})
              </option>
            ))}
          </select>
        </div>
      </div>

      {warehouseBooksLoading && <p className="admin-loading">Загрузка книг...</p>}
      {warehouseBooksError && <p className="admin-error">{warehouseBooksError}</p>}
      {selectedWarehouseId && !warehouseBooksLoading && !warehouseBooksError && (
        <div className="admin-warehouse-books-list">
          <h4>Книги на складе</h4>
          {warehouseBooks.length === 0 ? (
            <p>На этом складе нет книг.</p>
          ) : (
            warehouseBooks.map((book) => (
              <div key={book.bookId} className="admin-warehouse-book-item">
                <p>{book.bookTitle} - Количество: {book.stock}</p>
                <div className="admin-book-actions">
                  {editingBookStock?.bookId === book.bookId ? (
                    <form onSubmit={handleUpdateBookStock} className="admin-form-inline">
                      <input
                        type="number"
                        value={editingBookStock.stock}
                        onChange={(e) => setEditingBookStock({ ...editingBookStock, stock: e.target.value })}
                        min="0"
                        required
                      />
                      <button type="submit" className="edit-button">Сохранить</button>
                      <button type="button" onClick={() => setEditingBookStock(null)} className="cancel-button">Отмена</button>
                    </form>
                  ) : (
                    <>
                      <button onClick={() => handleEditBookStock(book)} className="edit-button">Редактировать</button>
                      <button onClick={() => handleDeleteBookFromWarehouse(book.bookId)} className="delete-button">Удалить</button>
                    </>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      )}

      <h3>Распределение книг по складам</h3>
      <form onSubmit={handleDistributeBook} className="admin-form">
        <div>
          <select name="bookId" value={distribution.bookId} onChange={handleDistributionChange} required>
            <option value="">Выберите книгу</option>
            {books.map((book) => (
              <option key={book.id} value={book.id}>{book.title}</option>
            ))}
          </select>
        </div>
        <div>
          <select name="warehouseId" value={distribution.warehouseId} onChange={handleDistributionChange} required>
            <option value="">Выберите склад</option>
            {warehouses.map((warehouse) => (
              <option key={warehouse.id} value={warehouse.id}>
                {warehouse.name} ({warehouse.city}, {warehouse.street}, {warehouse.houseNumber})
              </option>
            ))}
          </select>
        </div>
        <div>
          <input type="number" name="stock" placeholder="Количество" value={distribution.stock} onChange={handleDistributionChange} required min="1" />
        </div>
        <button type="submit">Распределить</button>
      </form>
    </div>
  );
};

export default AdminPanel;