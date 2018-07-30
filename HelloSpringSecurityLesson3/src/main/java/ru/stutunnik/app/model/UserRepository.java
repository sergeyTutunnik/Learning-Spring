package ru.stutunnik.app.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class UserRepository {

    private static final List<User> inMemoryUsers = new ArrayList<>();

    public UserRepository() {
        //Creating in memory users

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Stored passwords should be encoded

        GrantedAuthority adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority adminAuth2 = new SimpleGrantedAuthority("ADMIN");

        inMemoryUsers.add(new User(
                1l,
                "q1",
                encoder.encode("1"),
                new HashSet<>()
        ));
        inMemoryUsers.add(new User(2l,
                "admin",
                encoder.encode("1"),
                new HashSet<>(Arrays.asList(adminAuth, adminAuth2))
        ));
    }

    public List<User> finfAllUsers() {

        return inMemoryUsers;
    }

    public User findUserByName(String userName) {

        User foundUser = new User();
        for (User user : inMemoryUsers) {
            if (user.getName().equals(userName)) {
                foundUser = user;
            }
        }

        return foundUser;
    }
}
