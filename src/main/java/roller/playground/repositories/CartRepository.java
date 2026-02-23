package roller.playground.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roller.playground.entities.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends CrudRepository<Cart, UUID> {
    @EntityGraph(attributePaths = {"items.product"})
    @Query("select c from Cart c where c.id = :id")
    Optional<Cart> getCartWithProducts(@Param("id") UUID userId);
}
