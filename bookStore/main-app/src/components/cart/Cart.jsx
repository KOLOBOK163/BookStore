import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import './Cart.css';

const Cart = () => {
  const { cart, removeFromCart, updateCartQuantity, clearCart } = useAuth();
  const navigate = useNavigate();

  const total = cart.reduce((sum, item) => {
    const price = item.discountedPrice || item.price; // Используем цену со скидкой, если она есть
    return sum + price * item.quantity;
  }, 0);

  const handleCheckout = () => {
    navigate('/orders');
  };

  return (
    <div className="cart-container">
      <h1 className="cart-title">Корзина</h1>
      {cart.length === 0 ? (
        <div className="cart-empty-state">
          <p className="cart-empty-text">Корзина пуста.</p>
          <Link to="/" className="cart-shop-now">Продолжить покупки</Link>
        </div>
      ) : (
        <>
          <div className="cart-items">
            {cart.map((item) => (
              <div key={item.id} className="cart-item-card">
                <div className="cart-item-details">
                  <Link to={`/book/${item.id}`} className="cart-item-link">
                    {item.title}
                  </Link>
                  {item.activeDiscount ? (
                    <p className="cart-item-price">
                      <span style={{ textDecoration: 'line-through', color: 'gray' }}>
                        {item.price} ₽ x {item.quantity}
                      </span>{' '}
                      <span style={{ color: 'red' }}>
                        {item.discountedPrice} ₽ x {item.quantity} (-{item.activeDiscount.discountPercentage}%)
                      </span>
                    </p>
                  ) : item.discountPercentage && item.discountPercentage > 0 ? (
                    <p className="cart-item-price">
                      <span style={{ textDecoration: 'line-through', color: 'gray' }}>
                        {item.price} ₽ x {item.quantity}
                      </span>{' '}
                      <span style={{ color: 'red' }}>
                        {item.discountedPrice} ₽ x {item.quantity} (-{item.discountPercentage}%)
                      </span>
                    </p>
                  ) : (
                    <p className="cart-item-price">{item.price} ₽ x {item.quantity}</p>
                  )}
                </div>
                <div className="cart-item-controls">
                  <button
                    onClick={() => updateCartQuantity(item.id, item.quantity - 1)}
                    disabled={item.quantity <= 1}
                  >
                    -
                  </button>
                  <span className="cart-item-quantity">{item.quantity}</span>
                  <button
                    onClick={() => updateCartQuantity(item.id, item.quantity + 1)}
                  >
                    +
                  </button>
                  <button
                    onClick={() => removeFromCart(item.id)}
                    className="cart-item-remove"
                  >
                    🗑️
                  </button>
                </div>
              </div>
            ))}
          </div>
          <div className="cart-summary">
            <div className="cart-total">
              <p className="cart-total-text">Итого: <span className="cart-total-amount">{total} ₽</span></p>
            </div>
            <div className="cart-actions">
              <button onClick={clearCart} className="cart-clear-button">
                Очистить корзину
              </button>
              <button onClick={handleCheckout} className="cart-checkout-button">
                Оформить заказ
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default Cart;