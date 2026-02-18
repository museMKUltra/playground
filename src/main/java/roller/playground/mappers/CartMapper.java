package roller.playground.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import roller.playground.dtos.CartDto;
import roller.playground.dtos.CartItemDto;
import roller.playground.entities.Cart;
import roller.playground.entities.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
