package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roller.playground.entities.Address;
import roller.playground.entities.Profile;
import roller.playground.entities.User;
import roller.playground.repositories.ProfileRepository;
import roller.playground.repositories.UserRepository;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public void createUser() {
        var profile = Profile.builder()
                .bio("bio20")
                .loyaltyPoints(20)
                .build();
        var user = User.builder()
                .name("John Doe20")
                .email("email20")
                .password("password")
                .build();
        var address = Address.builder()
                .street("Via Roma 20")
                .zip("20")
                .city("Roma")
                .state("RM")
                .build();
        user.addAddress(address);
        user.addProfile(profile);
        userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteAddress(Long id) {
        var user = userRepository.findById(id).orElseThrow();
        var address = user.getAddresses().getFirst();
        user.removeAddress(address);
        userRepository.save(user);
    }

    @Transactional
    public void fetchUser() {
        var user = userRepository.findByEmail("emial2").orElseThrow();
        System.out.println(user);
    }

    public void fetchUsers() {
        for (User user : userRepository.findAllWithAddresses()) {
            System.out.println(user);
            user.getAddresses().forEach(System.out::println);
        }
    }

    @Transactional
    public void fetchProfile() {
        var profiles = profileRepository.findByLoyaltyPointsGreaterThanOrderByUserEmail(2);
        profiles.forEach(p -> {
            System.out.println(p);
            System.out.println(p.getId());
            System.out.println(p.getUser().getEmail());
        });
    }
}
