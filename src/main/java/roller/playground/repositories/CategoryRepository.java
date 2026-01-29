package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}
