package com.jpetstore.api.controller;

import com.jpetstore.api.dto.CartDto;
import com.jpetstore.api.dto.CartItemDto;
import com.jpetstore.api.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getCart(HttpSession session) {
        String sessionId = session.getId();
        CartDto cart = cartService.getCart(sessionId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDto> addToCart(@RequestBody CartItemDto item, HttpSession session) {
        String sessionId = session.getId();
        CartDto cart = cartService.addToCart(sessionId, item);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartDto> updateCartItem(
            @PathVariable String itemId,
            @RequestParam int quantity,
            HttpSession session) {
        String sessionId = session.getId();
        CartDto cart = cartService.updateCartItem(sessionId, itemId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<CartDto> removeFromCart(@PathVariable String itemId, HttpSession session) {
        String sessionId = session.getId();
        CartDto cart = cartService.removeFromCart(sessionId, itemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        String sessionId = session.getId();
        cartService.clearCart(sessionId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Cart cleared successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getCartCount(HttpSession session) {
        String sessionId = session.getId();
        int count = cartService.getCartItemCount(sessionId);
        
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }
}
