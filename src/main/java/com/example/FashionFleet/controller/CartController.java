package com.example.FashionFleet.controller;

import com.example.FashionFleet.util.error.IdInvalidException;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.FashionFleet.domain.Cart;
import com.example.FashionFleet.service.CartService;
import com.example.FashionFleet.util.annotation.ApiMessage;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/cart/{cartId}")
    @ApiMessage("Fetch user cart by ID")
    public ResponseEntity<Cart> getCart(@PathVariable("cartId") Long cartId) throws IdInvalidException {
        Cart cart = cartService.getCart(cartId);
        if(cart == null){
            throw new IdInvalidException("Cart ID"+ cartId +"does not exist");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(cart);
    }

    @DeleteMapping("/cart/{cartId}")
    @ApiMessage("Clear all items in the cart")
    public ResponseEntity<Void> clearCart(@PathVariable("cartId") Long cartId) throws IdInvalidException{
        cartService.clearCart(cartId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/cart/total-price/{cartId}")
    @ApiMessage("Fetch total price of all items in cart")
    public ResponseEntity<BigDecimal> getTotalAmount(@PathVariable("cartId") Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(totalPrice);
    }

}