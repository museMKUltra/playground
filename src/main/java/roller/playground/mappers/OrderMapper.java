package roller.playground.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import roller.playground.dtos.CheckoutResponseDto;
import roller.playground.dtos.OrderDto;
import roller.playground.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "orderId", source = "id")
    CheckoutResponseDto toCheckoutDto(Order order);

    @Mapping(target = "totalPrice", expression = "java(order.getTotalPrice())")
    @Mapping(target = "items", source = "orderItems")
    OrderDto toDto(Order order);
}
