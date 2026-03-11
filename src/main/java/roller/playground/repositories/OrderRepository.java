package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
}