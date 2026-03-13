package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import roller.playground.dtos.CheckoutResponseDto;
import roller.playground.entities.Order;
import roller.playground.excpetions.CartEmptyException;
import roller.playground.excpetions.CartNotFoundException;
import roller.playground.mappers.OrderMapper;
import roller.playground.repositories.CartRepository;
import roller.playground.repositories.OrderRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponseDto checkout(UUID cartId) {
        var cart = cartRepository.getCartWithProducts(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException();
        }

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);
        cartService.deleteCartItems(cart.getId());

        return orderMapper.toCheckoutDto(order);
    }
}
