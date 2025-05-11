import React, { useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import { topUpBalanceWithCard } from '../services/api';
import { toast } from 'react-toastify';
import './ProfilePage.css';

const ProfilePage = () => {
  const { user, setUser } = useAuth();
  const [amount, setAmount] = useState('');
  const [cardData, setCardData] = useState({ cardNumber: '', expiryDate: '', cvv: '' });
  const [isProcessing, setIsProcessing] = useState(false);

  const validateCardDetails = () => {
    const cleanCardNumber = cardData.cardNumber.replace(/\s/g, '');
    if (cleanCardNumber.length !== 16 || !/^\d+$/.test(cleanCardNumber)) {
      toast.error('Номер карты должен содержать 16 цифр');
      return false;
    }
    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(cardData.expiryDate)) {
      toast.error('Срок действия должен быть в формате MM/YY');
      return false;
    }
    const [month, year] = cardData.expiryDate.split('/');
    const currentYear = new Date().getFullYear() % 100;
    const currentMonth = new Date().getMonth() + 1;
    if (
      parseInt(month) < 1 ||
      parseInt(month) > 12 ||
      parseInt(year) < currentYear ||
      (parseInt(year) === currentYear && parseInt(month) < currentMonth)
    ) {
      toast.error('Срок действия карты истёк или указан неверно');
      return false;
    }
    if (cardData.cvv.length !== 3 || !/^\d+$/.test(cardData.cvv)) {
      toast.error('CVV должен содержать 3 цифры');
      return false;
    }
    return true;
  };

  const handleTopUp = async () => {
    if (!amount || parseFloat(amount) <= 0) {
      toast.error('Сумма должна быть больше 0');
      return;
    }
    if (!cardData.cardNumber || !cardData.expiryDate || !cardData.cvv) {
      toast.error('Пожалуйста, заполните все поля карты');
      return;
    }

    if (!validateCardDetails()) {
      return;
    }

    setIsProcessing(true);
    try {
      await new Promise((resolve) => setTimeout(resolve, 2000));
      const cleanCardNumber = cardData.cardNumber.replace(/\s/g, '');
      const response = await topUpBalanceWithCard({
        amount: parseFloat(amount),
        cardNumber: cleanCardNumber,
        expiryDate: cardData.expiryDate,
        cvv: cardData.cvv
      });
      setUser(response);
      toast.success('Баланс успешно пополнен!');
      setAmount('');
      setCardData({ cardNumber: '', expiryDate: '', cvv: '' });
    } catch (err) {
      toast.error('Ошибка пополнения баланса: ' + (err.response?.data?.message || 'Проверьте данные карты'));
      console.error(err);
    } finally {
      setIsProcessing(false);
    }
  };

  const handleCardChange = (e) => {
    const { name, value } = e.target;
    if (name === 'cardNumber') {
      const digitsOnly = value.replace(/\D/g, '');
      const formattedValue = digitsOnly
        .slice(0, 16)
        .replace(/(\d{4})(?=\d)/g, '$1 ');
      setCardData({ ...cardData, cardNumber: formattedValue });
    } else if (name === 'expiryDate') {
      const digitsOnly = value.replace(/\D/g, '');
      let formattedValue = digitsOnly.slice(0, 4);
      if (digitsOnly.length >= 2) {
        formattedValue = `${digitsOnly.slice(0, 2)}/${digitsOnly.slice(2, 4)}`;
      }
      setCardData({ ...cardData, expiryDate: formattedValue });
    } else if (name === 'cvv') {
      const digitsOnly = value.replace(/\D/g, '').slice(0, 3);
      setCardData({ ...cardData, cvv: digitsOnly });
    }
  };

  if (!user) return <p>Пожалуйста, войдите в аккаунт</p>;

  return (
    <div className="profile-container">
      <h2 className="profile-title">Профиль</h2>
      <div className="profile-info">
        <p><strong>Логин:</strong> {user.login}</p>
        <p><strong>Email:</strong> {user.email}</p>
        <p><strong>Баланс:</strong> {user.balance || 0} ₽</p>
      </div>
      <div className="topup-card">
        <h3 className="topup-title">Пополнить баланс</h3>
        <div className="topup-form">
          <input
            type="number"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            placeholder="Сумма"
            disabled={isProcessing}
            className="topup-input"
          />
          <input
            type="text"
            name="cardNumber"
            value={cardData.cardNumber}
            onChange={handleCardChange}
            placeholder="Номер карты (16 цифр)"
            maxLength="19"
            disabled={isProcessing}
            className="topup-input"
          />
          <div className="topup-row">
            <input
              type="text"
              name="expiryDate"
              value={cardData.expiryDate}
              onChange={handleCardChange}
              placeholder="Срок действия (MM/YY)"
              maxLength="5"
              disabled={isProcessing}
              className="topup-input short"
            />
            <input
              type="text"
              name="cvv"
              value={cardData.cvv}
              onChange={handleCardChange}
              placeholder="CVV (3 цифры)"
              maxLength="3"
              disabled={isProcessing}
              className="topup-input short"
            />
          </div>
          <button onClick={handleTopUp} disabled={isProcessing} className="topup-button">
            {isProcessing ? 'Обработка...' : 'Пополнить'}
          </button>
        </div>
      </div>
      {user.transactions && user.transactions.length > 0 && (
        <div className="transaction-history">
          <h3 className="transaction-title">История транзакций</h3>
          <ul className="transaction-list">
            {user.transactions.map((tx, index) => (
              <li key={index} className="transaction-item">
                {new Date(tx.date).toLocaleString()} - <strong>{tx.amount} ₽</strong> - {tx.status} (Карта: **** **** **** {tx.cardLastFour})
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default ProfilePage;