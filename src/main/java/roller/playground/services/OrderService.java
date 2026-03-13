package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import roller.playground.dtos.OrderDto;
import roller.playground.excpetions.OrderNotFoundException;
import roller.playground.mappers.OrderMapper;
import roller.playground.repositories.OrderRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final AuthService authService;

    public List<OrderDto> getOrders() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.findAllByCustomer(user);
        var ordersDto = orders.stream().map(orderMapper::toDto);

        return ordersDto.toList();
    }

    public OrderDto getOrder(Long id) {
        var order = orderRepository
                .findById(id)
                .orElseThrow(OrderNotFoundException::new);
        var user = authService.getCurrentUser();

        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException(
                    "You don't have permission to access this order"
            );
        }

        return orderMapper.toDto(order);
    }
}
