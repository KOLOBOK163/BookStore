import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token && !config.url.includes('/auth/register') && !config.url.includes('/auth/login')) {
      config.headers.Authorization = `Bearer ${token}`;
      console.log('Sending request:', config.method, config.url, 'with token:', token, 'headers:', config.headers);
    } else {
      console.warn('No token or skipping token for request:', config.method, config.url);
    }
    if (config.method.toLowerCase() === 'put') {
      config.headers['Content-Type'] = 'application/json';
    }
    return config;
  },
  (error) => {
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

api.interceptors.response.use(
  (response) => {
    console.log('Response:', response.status, response.config.method, response.config.url, response.data);
    return response.data;
  },
  (error) => {
    if (error.response) {
      console.error('Error Response:', error.response.status, error.response.config.method, error.response.config.url, error.response.data, 'headers:', error.response.headers);
      if (error.response.status === 403) {
        console.error('403 Forbidden:', error.response.data);
        console.error('Token:', localStorage.getItem('token'));
      }
    } else if (error.request) {
      console.error('No response received:', error.request);
    } else {
      console.error('Error:', error.message);
    }
    return Promise.reject(error);
  }
);

export const loginUser = (loginData) => api.post('/auth/login', loginData);
export const registerUser = (registerData) => api.post('/auth/register', registerData);
export const getBooks = () => api.get('/books');
export const getBookById = (id) => api.get(`/books/${id}`);
export const searchBooks = (title) => api.get(`/books/search?title=${title}`);
export const getAddresses = () => api.get('/addresses');
export const addAddress = (addressData) => api.post('/addresses', addressData);
export const createOrder = (orderData) => api.post('/orders', orderData);
export const getOrders = () => api.get('/orders');
export const getBonusCard = () => api.get('/orders/bonus-cards');
export const topUpBalanceWithCard = (data) => api.post('/customer/topup-with-card', data);
export const getCustomerProfile = () => api.get('/customer/me');
export const createBook = (bookData, coverImage) => {
  if (coverImage) {
    const formData = new FormData();
    formData.append('book', JSON.stringify(bookData));
    formData.append('coverImage', coverImage);
    console.log('FormData created:', Array.from(formData.entries()));
    return api.post('/admin/books', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
  console.log('Sending JSON data:', bookData);
  return api.post('/admin/books', bookData, {
    headers: { 'Content-Type': 'application/json' }
  });
};

export const updateBook = (id, bookData, coverImage) => {
  if (coverImage) {
    const formData = new FormData();
    formData.append('book', JSON.stringify(bookData));
    formData.append('coverImage', coverImage);
    console.log('FormData created:', Array.from(formData.entries()));
    return api.put(`/admin/books/${id}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
  console.log('Sending JSON data:', bookData);
  return api.put(`/admin/books/${id}`, bookData, {
    headers: { 'Content-Type': 'application/json' }
  });
};
export const deleteBook = (id) => api.delete(`/books/${id}`);
export const getAllWarehouses = () => api.get('/warehouses');
export const createWarehouse = (warehouseData) => api.post('/warehouses', warehouseData);
export const updateWarehouse = (id, warehouseData) => api.put(`/warehouses/${id}`, warehouseData);
export const deleteWarehouse = (id) => api.delete(`/warehouses/${id}`);
export const distributeBook = (bookId, warehouseId, stock) => api.post('/books/distribute', null, {
  params: { bookId, warehouseId, stock }
});
export const getBooksInWarehouse = (warehouseId) => api.get(`/warehouses/${warehouseId}/books`);
export const updateBookStock = (warehouseId, bookId, bookWarehouseDTO) => api.put(`/warehouses/${warehouseId}/books/${bookId}`, bookWarehouseDTO);
export const deleteBookFromWarehouse = (warehouseId, bookId) => api.delete(`/warehouses/${warehouseId}/books/${bookId}`);

// Методы для работы со скидками
export const createDiscount = (bookId, discountData) => api.post(`/books/${bookId}/discounts`, discountData);
export const updateDiscount = (id, discountData) => api.put(`/books/discounts/${id}`, discountData);
export const deleteDiscount = (id) => api.delete(`/books/discounts/${id}`);
export const getDiscountsForBook = (bookId) => api.get(`/books/${bookId}/discounts`);

export default api;