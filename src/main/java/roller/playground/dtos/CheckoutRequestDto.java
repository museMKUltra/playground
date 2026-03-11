package roller.playground.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequestDto {
    @NotNull(message = "Cart id cannot be null")
    private UUID cartId;
}
