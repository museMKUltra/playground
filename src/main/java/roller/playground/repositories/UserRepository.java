package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
