import React from 'react';
import OrderForm from '../components/order/OrderForm';
import OrderHistory from '../components/order/OrderHistory';
import { useAuth } from '../hooks/useAuth';
import { Navigate } from 'react-router-dom';

const OrderPage = () => {
  const { user } = useAuth();

  if (!user) {
    return <Navigate to="/login" />;
  }

  return (
    <div>
      <OrderForm />
      <OrderHistory />
    </div>
  );
};

export default OrderPage;