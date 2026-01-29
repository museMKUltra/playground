package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
