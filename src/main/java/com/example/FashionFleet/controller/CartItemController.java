package com.example.FashionFleet.controller;

import com.example.FashionFleet.util.error.IdInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.FashionFleet.service.CartItemService;
import com.example.FashionFleet.service.CartService;
import com.example.FashionFleet.util.annotation.ApiMessage;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartItemController(CartItemService cartItemService, CartService cartService) {
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @PostMapping("/cart/items")
    @ApiMessage("Add to cart")
    public ResponseEntity<Void> addItemToCart(@RequestParam(required = false, name = "cartId") Long cartId,
                                              @RequestParam(name = "productId") Long productId,
                                              @RequestParam(name = "quantity") Integer quantity) throws IdInvalidException{
            if (cartId == null) {
                cartId = cartService.initializeNewCart();
            }
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(null);
    }

    @DeleteMapping("/cart/items")
    @ApiMessage("remove item from cart")
    public ResponseEntity<Void> removeItemFromCart(@RequestParam(name = "cartId") Long cartId,
                                                     @RequestParam(name = "itemId") Long itemId) throws IdInvalidException {
        cartItemService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<Void> updateItemQuantity(@PathVariable("cartId") Long cartId,
            @PathVariable("itemId") Long itemId,
            @RequestParam(name = "quantity") Integer quantity) throws  IdInvalidException{
        cartItemService.updateItemQuantity(cartId, itemId, quantity);
        return ResponseEntity.ok(null);
    }
}
