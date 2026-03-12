package roller.playground.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import roller.playground.entities.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomerId(Long customerId);
}