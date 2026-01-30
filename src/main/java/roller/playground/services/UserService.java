package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roller.playground.entities.Address;
import roller.playground.entities.Profile;
import roller.playground.entities.User;
import roller.playground.repositories.UserRepository;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void createUser() {
        var profile = Profile.builder()
                .bio("bio3")
                .build();
        var user = User.builder()
                .name("John Doe3")
                .email("emial")
                .password("password")
                .build();
        var address = Address.builder()
                .street("Via Roma 3")
                .zip("00300")
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
}
