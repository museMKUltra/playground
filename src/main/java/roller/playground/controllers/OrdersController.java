package roller.playground.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roller.playground.dtos.OrderDto;
import roller.playground.excpetions.BelongToOtherUserException;
import roller.playground.excpetions.OrderNotFoundException;
import roller.playground.services.OrderService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
class OrdersController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        var orderDto = orderService.getOrder(id);

        return ResponseEntity.ok(orderDto);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Order not found")
        );
    }

    @ExceptionHandler(BelongToOtherUserException.class)
    public ResponseEntity<Map<String, String>> handleBelongToOtherUser() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                Map.of("error", "Order belongs to other user")
        );
    }
}
