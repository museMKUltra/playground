package roller.playground.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roller.playground.dtos.CheckoutRequestDto;
import roller.playground.entities.Order;
import roller.playground.entities.OrderStatus;
import roller.playground.mappers.OrderMapper;
import roller.playground.repositories.CartRepository;
import roller.playground.repositories.OrderRepository;
import roller.playground.repositories.UserRepository;
import roller.playground.services.AuthService;
import roller.playground.services.CartService;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/checkout")
class CheckoutController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CartService cartService;

    @RequestMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequestDto checkoutRequestDto
    ) {
        var cart = cartRepository.getCartWithProducts(checkoutRequestDto.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart not found")
            );
        }

        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart is empty")
            );
        }

        var order = Order.builder()
                .customer(authService.getCurrentUser())
                .totalPrice(cart.getTotalPrice())
                .status(OrderStatus.PENDING)
                .build();

        cart.getItems().forEach(item -> {
            var product = item.getProduct();
            order.addOrderItem(product, item.getQuantity(), product.getPrice(), item.getTotalPrice());
        });

        orderRepository.save(order);
        cartService.deleteCartItems(cart.getId());

        return ResponseEntity.ok().body(orderMapper.toCheckoutDto(order));
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
