package roller.playground.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import roller.playground.dtos.CheckoutRequestDto;
import roller.playground.dtos.CheckoutResponseDto;
import roller.playground.dtos.ErrorDto;
import roller.playground.excpetions.CartEmptyException;
import roller.playground.excpetions.CartNotFoundException;
import roller.playground.mappers.OrderMapper;
import roller.playground.repositories.CartRepository;
import roller.playground.repositories.OrderRepository;
import roller.playground.repositories.UserRepository;
import roller.playground.services.AuthService;
import roller.playground.services.CartService;
import roller.playground.services.CheckoutService;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
class CheckoutController {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final CheckoutService checkoutService;

    @PostMapping
    public CheckoutResponseDto checkout(
            @Valid @RequestBody CheckoutRequestDto checkoutRequestDto
    ) {
        return checkoutService.checkout(checkoutRequestDto.getCartId());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException exception) {
        var errors = new HashMap<String, String>();
        exception.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleCartNotFound(Exception ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
