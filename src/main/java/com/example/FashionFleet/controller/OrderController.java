package com.example.FashionFleet.controller;



import com.example.FashionFleet.domain.Order;
import com.example.FashionFleet.domain.dto.response.order.OrderDTO;
import com.example.FashionFleet.service.OrderService;
import com.example.FashionFleet.util.annotation.ApiMessage;
import com.example.FashionFleet.util.error.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    @ApiMessage("Order")
    public ResponseEntity<Object> createOrder(@RequestParam("userId") Long userId) {
        try {
            OrderDTO order =  orderService.placeOrder(userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable("orderId") Long orderId) throws ResourceNotFoundException{
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return ResponseEntity.ok( order);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body( e.getMessage());
        }
    }

    @GetMapping("/order/user/{userId}")
    public ResponseEntity<Object> getUserOrders(@PathVariable("userId") Long userId) throws ResourceNotFoundException {
        try {
            List<OrderDTO> order = orderService.getUserOrders(userId);
            return ResponseEntity.ok( order);
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body( e.getMessage());
        }
    }
}
