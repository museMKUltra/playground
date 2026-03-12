package roller.playground.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import roller.playground.dtos.OrderDto;
import roller.playground.excpetions.OrderNotFoundException;
import roller.playground.mappers.OrderMapper;
import roller.playground.repositories.OrderRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
@AllArgsConstructor
class OrdersController {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @RequestMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        var orders = orderRepository.findAllByCustomerId(userId);
        var ordersDto = orders.stream().map(orderMapper::toDto);

        return ResponseEntity.ok().body(ordersDto.toList());
    }

    @RequestMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        var order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new OrderNotFoundException();
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();
        if (order.getCustomerId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(OrderNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Order not found")
        );
    }
}
