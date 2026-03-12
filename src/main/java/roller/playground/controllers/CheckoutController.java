package roller.playground.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Controller
@RequestMapping("/checkout")
class CheckoutController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @RequestMapping
    public ResponseEntity checkout(
            @Valid @RequestBody CheckoutRequestDto checkoutRequestDto
    ) {
        var cart = cartRepository.getCartWithProducts(checkoutRequestDto.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.notFound().build();
        }
        cart.getItems().forEach(item -> {
            System.out.println(item.getProduct().getName() + " - " + item.getQuantity());
        });

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var order = Order.builder()
                .customerId(userId)
                .totalPrice(cart.getTotalPrice())
                .status(OrderStatus.PENDING)
                .build();

        cart.getItems().forEach(item -> {
            var product = item.getProduct();
            order.addOrderItem(product, item.getQuantity(), product.getPrice(), item.getTotalPrice());
        });

        orderRepository.save(order);
        cartRepository.delete(cart);

        return ResponseEntity.ok().body(orderMapper.toDto(order));
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
