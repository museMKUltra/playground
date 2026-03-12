package roller.playground.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<OrderItemDto> items = new ArrayList<>();
}
