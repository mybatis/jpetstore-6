package com.jpetstore.api.service;

import com.jpetstore.api.dto.CartDto;
import com.jpetstore.api.dto.CartItemDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartService {

    // In-memory storage for carts (session-based)
    private final Map<String, CartDto> carts = new ConcurrentHashMap<>();
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08"); // 8% tax

    public CartDto getCart(String sessionId) {
        return carts.computeIfAbsent(sessionId, k -> new CartDto());
    }

    public CartDto addToCart(String sessionId, CartItemDto item) {
        CartDto cart = getCart(sessionId);
        
        // Check if item already exists in cart
        boolean found = false;
        for (CartItemDto existingItem : cart.getItems()) {
            if (existingItem.getItemId().equals(item.getItemId())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                existingItem.setTotal(existingItem.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
                found = true;
                break;
            }
        }
        
        if (!found) {
            item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            cart.getItems().add(item);
        }
        
        recalculateCart(cart);
        return cart;
    }

    public CartDto updateCartItem(String sessionId, String itemId, int quantity) {
        CartDto cart = getCart(sessionId);
        
        if (quantity <= 0) {
            return removeFromCart(sessionId, itemId);
        }
        
        for (CartItemDto item : cart.getItems()) {
            if (item.getItemId().equals(itemId)) {
                item.setQuantity(quantity);
                item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
                break;
            }
        }
        
        recalculateCart(cart);
        return cart;
    }

    public CartDto removeFromCart(String sessionId, String itemId) {
        CartDto cart = getCart(sessionId);
        cart.getItems().removeIf(item -> item.getItemId().equals(itemId));
        recalculateCart(cart);
        return cart;
    }

    public void clearCart(String sessionId) {
        carts.remove(sessionId);
    }

    public int getCartItemCount(String sessionId) {
        CartDto cart = getCart(sessionId);
        return cart.getItems().stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();
    }

    private void recalculateCart(CartDto cart) {
        BigDecimal subtotal = BigDecimal.ZERO;
        int itemCount = 0;
        
        for (CartItemDto item : cart.getItems()) {
            subtotal = subtotal.add(item.getTotal());
            itemCount += item.getQuantity();
        }
        
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax);
        
        cart.setSubtotal(subtotal);
        cart.setTax(tax);
        cart.setTotal(total);
        cart.setItemCount(itemCount);
    }
}
