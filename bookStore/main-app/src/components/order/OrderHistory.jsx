import React, { useEffect } from 'react';
import { useAuth } from '../../hooks/useAuth';
import { useOrders } from '../../hooks/useOrders';
import './OrderHistory.css';

const OrderHistory = () => {
  const { user, token } = useAuth();
  const { orders, setOrders, fetchOrders, loading, error } = useOrders();

  useEffect(() => {
    if (user && token) {
      fetchOrders();
    } else {
      setOrders([]);
    }
  }, [user, token]);

  if (loading) return <p className="order-history-loading">Загрузка...</p>;
  if (error) return <p className="order-history-error">{error}</p>;

  return (
    <div className="order-history-container">
      <h2>История заказов</h2>
      {orders && orders.length > 0 ? (
        <div>
          {orders.map((order, index) => (
            <div key={order.id} className="order-history-item">
              <p><strong>Заказ №{index + 1}</strong></p>
              <p>Дата: {new Date(order.orderDate).toLocaleDateString()}</p>
              <p>Статус: {order.status}</p>
              <p>Итого: {order.total || 0} ₽</p>
              <p>
                Адрес доставки: 
                {[
                  order.deliveryAddress?.city,
                  order.deliveryAddress?.street,
                  order.deliveryAddress?.house,
                  order.deliveryAddress?.apartment,
                ]
                  .filter(Boolean)
                  .join(', ')}
              </p>
              <p>Товары:</p>
              <ul>
                {order.orderItems?.map((item) => (
                  <li key={item.bookId}>
                    Название: {item.title}, Кол-во: {item.quantity}, Цена: {item.priceAtPurchase} ₽
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      ) : (
        <p className="order-history-empty">У вас пока нет заказов.</p>
      )}
    </div>
  );
};

export default OrderHistory;