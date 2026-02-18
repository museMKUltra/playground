package roller.playground.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemRequest {
    @NotNull(message = "Product id cannot be null")
    private Long productId;
}
