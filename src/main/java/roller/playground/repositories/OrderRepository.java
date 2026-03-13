package roller.playground.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import roller.playground.entities.Order;
import roller.playground.entities.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(User customer);
}