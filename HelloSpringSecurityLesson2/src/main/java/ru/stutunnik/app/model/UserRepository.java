package ru.stutunnik.app.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {

    private static final List<User> inMemoryUsers = new ArrayList<>();


    public UserRepository() {

        inMemoryUsers.add(new User(1l, "q1", "1"));
        inMemoryUsers.add(new User(2l, "admin", "1"));
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
