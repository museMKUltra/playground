package roller.playground.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import roller.playground.dtos.CheckoutResponseDto;
import roller.playground.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    CheckoutResponseDto toDto(Order order);
}
