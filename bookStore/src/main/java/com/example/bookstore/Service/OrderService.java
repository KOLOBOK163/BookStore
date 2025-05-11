package com.example.bookstore.Service;

import com.example.bookstore.DTO.BookDTO;
import com.example.bookstore.DTO.OrderDTO;
import com.example.bookstore.Entity.*;
import com.example.bookstore.Mapper.OrderMapper;
import com.example.bookstore.Repository.*;
import com.example.bookstore.Exception.BusinessException;
import com.example.bookstore.Exception.ResourceNotFoundException;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BonusCardRepository bonusCardRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BookService bookService;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        logger.info("Creating order for customer id: {}", orderDTO.getCustomerId());
        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + orderDTO.getCustomerId()));
        Address address = addressRepository.findById(orderDTO.getDeliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + orderDTO.getDeliveryAddressId()));

        Order order = orderMapper.toEntity(orderDTO);
        order.setCustomer(customer);
        order.setDeliveryAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("pending");
        order.setPaymentType("balance");
        order.setPaymentStatus("pending");

        int maxOrderNumber = orderRepository.findByCustomerId(customer.getId()).stream()
                .map(Order::getOrderNumber)
                .filter(num -> num != null && !num.isEmpty())
                .map(num -> {
                    try {
                        return Integer.parseInt(num.split("-")[1]);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compare)
                .orElse(0);
        order.setOrderNumber(String.format("ORD-%d-%d", customer.getId(), maxOrderNumber + 1));

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
            Book book = bookRepository.findById(itemDTO.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + itemDTO.getBookId()));
            logger.info("Processing book: {}, stock: {}, requested quantity: {}", book.getTitle(), book.getStock(), itemDTO.getQuantity());
            if (book.getStock() < itemDTO.getQuantity()) {
                throw new BusinessException("Not enough stock for book: " + book.getTitle());
            }
            book.setStock(book.getStock() - itemDTO.getQuantity());
            bookRepository.save(book);

            // Получаем цену с учётом активной скидки
            BookDTO bookDTO = bookService.getBookById(book.getId());
            BigDecimal priceAtPurchase = bookDTO.getDiscountedPrice() != null ? bookDTO.getDiscountedPrice() : bookDTO.getPrice();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPriceAtPurchase(priceAtPurchase);
            logger.info("OrderItem: bookId={}, quantity={}, priceAtPurchase={}", itemDTO.getBookId(), itemDTO.getQuantity(), priceAtPurchase);
            return orderItem;
        }).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BonusCard bonusCard = bonusCardRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Bonus card not found for customer: " + customer.getId()));

        if (customer.getBalance().compareTo(total) < 0) {
            throw new BusinessException("Недостаточно средств на балансе. Требуется: " + total + ", доступно: " + customer.getBalance());
        }
        customerService.updateBalance(customer.getId(), total.negate());
        order.setPaymentStatus("completed");

        order.setOrderItems(orderItems);
        order.setTotal(total);
        logger.info("Order total: {}", total);

        Order savedOrder = orderRepository.save(order);

        int newPoints = bonusCard.getPoints() + total.intValue() / 100;
        bonusCard.setPoints(newPoints);
        bonusCardRepository.save(bonusCard);

        return orderMapper.toDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByCustomer(Long customerId) {
        logger.info("Fetching orders for customer id: {}", customerId);
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        orders.forEach(order -> {
            Hibernate.initialize(order.getOrderItems());
            Hibernate.initialize(order.getDeliveryAddress());
        });
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
}