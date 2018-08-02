package ru.stutunnik.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import ru.stutunnik.app.model.User;
import ru.stutunnik.app.model.UserRepository;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String submitCreateUserForm(@ModelAttribute User user,
                                     @RequestParam Set<String> roles) {

        // TODO: 02.08.18 Implement custom PropertyEditor for authorities list 
        if(roles!=null && !roles.isEmpty()){
            Set<GrantedAuthority> authorities = new HashSet<>();
            roles.forEach(p->authorities.add(new SimpleGrantedAuthority(p)));
            user.setAuthorities(authorities);
        }

        userRepository.createUser(user);
        return "Success!!!";
    }


}
