package com.example.FashionFleet.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import com.example.FashionFleet.domain.User;
import com.example.FashionFleet.repository.UserRepository;
import com.example.FashionFleet.util.SecurityUtil;
import com.example.FashionFleet.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.FashionFleet.domain.Cart;
import com.example.FashionFleet.repository.CartItemRepository;
import com.example.FashionFleet.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    public Cart getCart(Long id) {
        Optional<Cart> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            return cart.get();
        }
        return null;
    }

    @Transactional
    public void clearCart(Long id) throws IdInvalidException {
        System.out.println("Clearing cart with ID: " + id);
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new IdInvalidException("Cart ID " + id + " does not exist"));
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cartRepository.deleteById(id);
    }

    public BigDecimal
    getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    public Long initializeNewCart() throws IdInvalidException{
        String currentUserLogin = SecurityUtil.getCurrentUserLogin().orElse(null);

        if (currentUserLogin != null) {
            // Find the user by email
            User currUser = userRepository.findByEmail(currentUserLogin);

            if (currUser != null) {
                // Check if the user already has a cart
                Cart existingCart = cartRepository.findByUserId(currUser.getId());

                if (existingCart != null) {
                    // If the user already has a cart, return the existing cart ID
                    return existingCart.getId();
                } else {
                    // Otherwise, create a new cart
                    Cart newCart = new Cart();
                    newCart.setUser(currUser);
                    Cart savedCart = cartRepository.save(newCart);
                    return savedCart.getId();  // Return the new cart's ID
                }
            } else {
                throw new IdInvalidException("User with email " + currentUserLogin + " not found.");
            }
        } else {
            throw new IdInvalidException("No user is currently logged in.");
        }

    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
