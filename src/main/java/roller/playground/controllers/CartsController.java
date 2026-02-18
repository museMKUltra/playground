package roller.playground.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import roller.playground.dtos.CartDto;
import roller.playground.dtos.CartItemDto;
import roller.playground.dtos.CartItemRequest;
import roller.playground.entities.Cart;
import roller.playground.entities.CartItem;
import roller.playground.mappers.CartMapper;
import roller.playground.repositories.CartRepository;
import roller.playground.repositories.ProductRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartsController {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var dto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addProductToCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartItemRequest cartItemRequest
    ) {
        var cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            return ResponseEntity.notFound().build();
        }

        var productId = cartItemRequest.getProductId();

        var product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            return ResponseEntity.badRequest().build();
        }

        var cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(1)
                    .build();
            cart.getCartItems().add(cartItem);
        } else {
            cartItem.incrementQuantity();
        }

        cartRepository.save(cart);

        var dto = cartMapper.toDto(cartItem);
        return ResponseEntity.created(null).body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
