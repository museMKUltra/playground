package roller.playground.mappers;

import org.mapstruct.Mapper;
import roller.playground.dtos.CartProductDto;
import roller.playground.entities.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    CartProductDto toDto(Product product);
}
