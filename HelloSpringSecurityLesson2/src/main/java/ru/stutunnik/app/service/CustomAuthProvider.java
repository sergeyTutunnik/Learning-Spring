package ru.stutunnik.app.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class CustomAuthProvider implements AuthenticationProvider {

    private final GrantedAuthority adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN");

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (authentication.getPrincipal().toString().equals("admin")) {
            grantedAuthorities.add(adminAuth);
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(), authentication.getCredentials(), grantedAuthorities);
        return auth;
    }


    public boolean supports(Class<?> aClass) {
        //return true;
        return false;
    }
}
