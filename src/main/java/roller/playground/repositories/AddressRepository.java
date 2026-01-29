package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Address;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
