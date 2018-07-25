package ru.stutunnik.app.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.stutunnik.app.model.User;

import java.util.Arrays;
import java.util.Collection;

public class CustomPrinipal implements UserDetails {
    private User user;

    public CustomPrinipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        GrantedAuthority adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN");
        GrantedAuthority adminAuth2 = new SimpleGrantedAuthority("ADMIN");
        return Arrays.asList(adminAuth, adminAuth2);
    }

    @Override
    public String getPassword() {
        //return user.getPassword();
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}