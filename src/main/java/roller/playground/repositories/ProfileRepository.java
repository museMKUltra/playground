package roller.playground.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import roller.playground.dtos.UserSummary;
import roller.playground.entities.Profile;

import java.util.List;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
    @EntityGraph(attributePaths = "user")
    @Query("select p.id as id, p.user.email as email from Profile p where p.loyaltyPoints > :points order by p.user.email")
    List<UserSummary> findByLoyaltyPoints(@Param("points") Integer loyaltyPoints);
}
