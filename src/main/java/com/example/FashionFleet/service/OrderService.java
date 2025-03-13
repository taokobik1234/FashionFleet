package com.example.FashionFleet.service;

import com.example.FashionFleet.domain.Cart;
import com.example.FashionFleet.domain.Order;
import com.example.FashionFleet.domain.OrderItem;
import com.example.FashionFleet.domain.Product;
import com.example.FashionFleet.domain.dto.response.order.OrderDTO;
import com.example.FashionFleet.repository.OrderRepository;
import com.example.FashionFleet.repository.ProductRepository;
import com.example.FashionFleet.util.enums.OrderStatus;
import com.example.FashionFleet.util.error.IdInvalidException;
import com.example.FashionFleet.util.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;


import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    @Transactional
    public OrderDTO placeOrder(Long userId) throws IdInvalidException {
        Cart cart   = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return  order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return  cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return  new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();

    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return  orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderDTO getOrder(Long orderId) throws ResourceNotFoundException{
        return orderRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public List<OrderDTO> getUserOrders(Long userId) throws ResourceNotFoundException{
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No orders found for user with ID: " + userId);
        }
        return  orders.stream().map(this :: convertToDto).toList();
    }

    private OrderDTO convertToDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
}
