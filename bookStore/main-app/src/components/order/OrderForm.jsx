import React, { useState, useEffect } from 'react';
import { useAuth } from '../../hooks/useAuth';
import { useAddresses } from '../../hooks/useAddresses';
import { useOrders } from '../../hooks/useOrders';
import { toast } from 'react-toastify';
import { getBonusCard } from '../../services/api';
import { Decimal } from 'decimal.js';
import './OrderForm.css';

const OrderForm = () => {
  const { cart, clearCart, user, token, refreshUser } = useAuth();
  const { addresses, fetchAddresses, createAddress, loading: addressesLoading, error: addressesError } = useAddresses();
  const { placeOrder, fetchOrders } = useOrders();
  const [address, setAddress] = useState({ city: '', street: '', house: '', apartment: '' });
  const [selectedAddress, setSelectedAddress] = useState('');
  const [showNewAddressForm, setShowNewAddressForm] = useState(false);
  const [bonusCard, setBonusCard] = useState({ number: '', points: 0 });
  const [deliveryEstimate, setDeliveryEstimate] = useState('');

  useEffect(() => {
    if (user && token) {
      fetchAddresses();
      fetchBonusCard();
    }
  }, [user, token]);

  const fetchBonusCard = async () => {
    try {
      const response = await getBonusCard();
      setBonusCard(response || { number: '', points: 0 });
    } catch (err) {
      console.error('Error fetching bonus card:', err);
    }
  };

  useEffect(() => {
    if (selectedAddress && addresses) {
      const selectedAddr = addresses.find(addr => addr.id === selectedAddress);
      if (selectedAddr) {
        calculateDeliveryEstimate(selectedAddr.city);
      }
    }
  }, [selectedAddress, addresses]);

  const handleAddressChange = (e) => {
    setAddress({ ...address, [e.target.name]: e.target.value });
  };

  const handleAddAddress = async () => {
    try {
      await createAddress({ city: address.city, street: address.street, house: address.house, apartment: address.apartment });
      await fetchAddresses();
      setShowNewAddressForm(false);
      setAddress({ city: '', street: '', house: '', apartment: '' });
      toast.success('Адрес добавлен!');
    } catch (err) {
      toast.error('Ошибка при добавлении адреса');
      console.error('Detailed error:', err.response?.data || err.message);
    }
  };

  const calculateDeliveryEstimate = (city) => {
    const baseDays = 3;
    const today = new Date();
    let additionalDays = 0;
    if (city.toLowerCase().includes('москва')) additionalDays = 0;
    else if (city.toLowerCase().includes('санкт-петербург')) additionalDays = 1;
    else additionalDays = 2;
    const deliveryDate = new Date(today.setDate(today.getDate() + baseDays + additionalDays));
    setDeliveryEstimate(`Ориентировочная доставка: ${deliveryDate.toLocaleDateString('ru-RU')} (${baseDays + additionalDays} дня)`);
  };

  const handleSubmitOrder = async () => {
    if (!user) {
      toast.error('Пожалуйста, войдите в аккаунт');
      return;
    }
    if (!selectedAddress) {
      toast.error('Пожалуйста, выберите адрес доставки.');
      return;
    }

    const total = cart.reduce((sum, item) => {
      const price = item.discountedPrice || item.price;
      return sum + price * item.quantity;
    }, 0);

    const balanceDecimal = new Decimal(user.balance || 0);
    if (balanceDecimal.lt(total)) {
      toast.error(`Недостаточно средств. Баланс: ${balanceDecimal.toString()}, требуется: ${total}`);
      return;
    }

    const orderData = {
      deliveryAddressId: selectedAddress,
      paymentType: 'balance',
      paymentStatus: 'pending',
      orderItems: cart.map((item) => ({
        bookId: item.id,
        quantity: item.quantity,
        priceAtPurchase: item.discountedPrice || item.price, // Используем цену со скидкой
      })),
    };

    try {
      await placeOrder(orderData);
      await refreshUser();
      clearCart();
      await fetchOrders();
      toast.success('Заказ успешно оформлен!');
    } catch (err) {
      toast.error('Ошибка при оформлении заказа');
      console.error(err);
    }
  };

  if (addressesLoading) return <p className="order-loading">Загрузка...</p>;
  if (addressesError) return <p className="order-error">{addressesError}</p>;

  return (
    <div className="order-form-container">
      <h2>Оформление заказа</h2>
      <div className="address-section">
        <h3>Выберите адрес доставки</h3>
        {addresses && addresses.length > 0 ? (
          <select value={selectedAddress} onChange={(e) => setSelectedAddress(e.target.value)}>
            <option value="">-- Выберите адрес --</option>
            {addresses.map((addr) => (
              <option key={addr.id} value={addr.id}>
                {addr.city}, {addr.street}, {addr.house}, {addr.apartment || ''}
              </option>
            ))}
          </select>
        ) : (
          <p>Адресов нет.</p>
        )}
        <button onClick={() => setShowNewAddressForm(!showNewAddressForm)}>
          {showNewAddressForm ? 'Отмена' : 'Добавить новый адрес'}
        </button>
      </div>

      {showNewAddressForm && (
        <div className="new-address-form">
          <h3>Новый адрес</h3>
          <input type="text" name="city" placeholder="Город" value={address.city} onChange={handleAddressChange} />
          <input type="text" name="street" placeholder="Улица" value={address.street} onChange={handleAddressChange} />
          <input type="text" name="house" placeholder="Дом" value={address.house} onChange={handleAddressChange} />
          <input type="text" name="apartment" placeholder="Квартира (опционально)" value={address.apartment} onChange={handleAddressChange} />
          <button onClick={handleAddAddress}>Сохранить адрес</button>
        </div>
      )}

      {deliveryEstimate && <p className="delivery-estimate">{deliveryEstimate}</p>}

      <div className="balance-section">
        <h3>Ваш баланс</h3>
        <p>{user.balance || 0} ₽</p>
      </div>

      <div className="bonus-section">
        <h3>Бонусная карта</h3>
        <p>Накоплено баллов: {bonusCard.points}</p>
      </div>

      <div className="order-items">
        <h3>Товары в заказе</h3>
        {cart.map((item) => (
          <div key={item.id} className="order-item">
            <span>{item.title} (x{item.quantity})</span>
            {item.activeDiscount ? (
              <span>
                <span style={{ textDecoration: 'line-through', color: 'gray' }}>
                  {item.price * item.quantity} ₽
                </span>{' '}
                <span style={{ color: 'red' }}>
                  {(item.discountedPrice || item.price) * item.quantity} ₽ (-{item.activeDiscount.discountPercentage}%)
                </span>
              </span>
            ) : item.discountPercentage && item.discountPercentage > 0 ? (
              <span>
                <span style={{ textDecoration: 'line-through', color: 'gray' }}>
                  {item.price * item.quantity} ₽
                </span>{' '}
                <span style={{ color: 'red' }}>
                  {(item.discountedPrice || item.price) * item.quantity} ₽ (-{item.discountPercentage}%)
                </span>
              </span>
            ) : (
              <span>{item.price * item.quantity} ₽</span>
            )}
          </div>
        ))}
        <p>
          Итого:{' '}
          {cart.reduce((sum, item) => {
            const price = item.discountedPrice || item.price;
            return sum + price * item.quantity;
          }, 0)}{' '}
          ₽
        </p>
        <button onClick={handleSubmitOrder}>Подтвердить заказ</button>
      </div>
    </div>
  );
};

export default OrderForm;