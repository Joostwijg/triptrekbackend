package com.triptrek.triptrek.config;

import com.triptrek.triptrek.model.User;
import com.triptrek.triptrek.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("moderator@test.com") == null) {
            User mod = new User();
            mod.setEmail("moderator@test.com");
            mod.setPassword(passwordEncoder.encode("Test1234!"));
            mod.setRole("moderator");
            mod.setFirstName("Mod");
            mod.setLastName("Erator");
            userRepository.save(mod);
        }

        if (userRepository.findByEmail("user@test.com") == null) {
            User user = new User();
            user.setEmail("user@test.com");
            user.setPassword(passwordEncoder.encode("Test1234!"));
            user.setRole("user");
            user.setFirstName("Us");
            user.setLastName("Er");
            userRepository.save(user);
        }
    }
}
