package roller.playground.mappers;

import org.mapstruct.Mapper;
import roller.playground.dtos.CartDto;
import roller.playground.entities.Cart;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
