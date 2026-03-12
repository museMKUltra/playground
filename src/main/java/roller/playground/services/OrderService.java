package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import roller.playground.dtos.OrderDto;
import roller.playground.excpetions.BelongToOtherUserException;
import roller.playground.excpetions.OrderNotFoundException;
import roller.playground.mappers.OrderMapper;
import roller.playground.repositories.OrderRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getOrders() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var orders = orderRepository.findAllByCustomerId(userId);
        var ordersDto = orders.stream().map(orderMapper::toDto);

        return ordersDto.toList();
    }

    public OrderDto getOrder(Long id) {
        var order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        if (!order.getCustomer().getId().equals(userId)) {
            throw new BelongToOtherUserException();
        }

        return orderMapper.toDto(order);
    }
}
