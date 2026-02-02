package roller.playground.repositories;

import org.springframework.data.repository.CrudRepository;
import roller.playground.entities.Profile;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    List<Profile> findProfilesByLoyaltyPointsGreaterThan(Integer loyaltyPoints);
}
