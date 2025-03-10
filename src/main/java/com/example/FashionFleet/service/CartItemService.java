package com.example.FashionFleet.service;

import java.math.BigDecimal;

import com.example.FashionFleet.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.FashionFleet.domain.Cart;
import com.example.FashionFleet.domain.CartItem;
import com.example.FashionFleet.domain.Product;
import com.example.FashionFleet.repository.CartItemRepository;
import com.example.FashionFleet.repository.CartRepository;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    public void addItemToCart(Long cartId, Long productId, int quantity) throws IdInvalidException{
        // 1. Get the cart
        // 2. Get the product
        // 3. Check if the product already in the cart
        // 4. If Yes, then increase the quantity with the requested quantity
        // 5. If No, then initiate a new CartItem entry.
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {
            throw new IdInvalidException("Cart not found with ID: " + cartId);
        }
        Product product = productService.fetchProductById(productId);
        if (product == null) {
            throw new IdInvalidException("Product not found with ID: " + productId);
        }
        CartItem cartItem = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(Long cartId, Long productId) throws  IdInvalidException{
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {
            throw new IdInvalidException("Cart not found with ID: " + cartId);
        }
        CartItem itemToRemove = getCartItem(cartId, productId);
        if(itemToRemove == null){
            throw new IdInvalidException("Item does not exist");
        }
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(Long cartId, Long productId, int quantity) throws  IdInvalidException{
        Cart cart = cartService.getCart(cartId);
        if (cart == null) {
            throw new IdInvalidException("Cart not found with ID: " + cartId);
        }
        CartItem itemToUpdate = cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IdInvalidException("Item with product ID " + productId + " not found in cart"));  // Handle item not found

        // Update the quantity, unit price, and total price
        itemToUpdate.setQuantity(quantity);
        itemToUpdate.setUnitPrice(itemToUpdate.getProduct().getPrice());  // Update unit price
        itemToUpdate.setTotalPrice();
        BigDecimal totalAmount = cart.getItems()
                .stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
    }
}
