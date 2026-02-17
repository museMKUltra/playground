package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Cart;

import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
}
