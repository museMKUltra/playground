package roller.playground.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Profile;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    @EntityGraph(attributePaths = "user")
    List<Profile> findByLoyaltyPointsGreaterThanOrderByUserEmail(Integer loyaltyPoints);
}
