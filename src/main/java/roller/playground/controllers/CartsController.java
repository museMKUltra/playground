package roller.playground.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import roller.playground.dtos.CartDto;
import roller.playground.dtos.CartItemDto;
import roller.playground.dtos.CartItemRequest;
import roller.playground.dtos.UpdateCartItemRequest;
import roller.playground.excpetions.CartNotFoundException;
import roller.playground.excpetions.ProductNotFoundException;
import roller.playground.services.CartService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
public class CartsController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addProductToCart(
            @PathVariable UUID cartId,
            @Valid @RequestBody CartItemRequest cartItemRequest
    ) {
        var productId = cartItemRequest.getProductId();
        var cartItemDto = cartService.addToCart(cartId, productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCartDto(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateCartItemQuantity(
            @Valid @RequestBody UpdateCartItemRequest request,
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ) {
        var quantity = request.getQuantity();
        return cartService.updateCartItemQuantity(cartId, productId, quantity);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ) {
        cartService.deleteCartItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> deleteCartItems(@PathVariable("cartId") UUID cartId) {
        cartService.deleteCartItems(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(CartNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart not found")
        );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound(ProductNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of("error", "Product not found in the cart")
        );
    }
}
