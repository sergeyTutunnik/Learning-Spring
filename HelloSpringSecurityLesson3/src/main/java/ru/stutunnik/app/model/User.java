package ru.stutunnik.app.model;

import org.springframework.security.core.GrantedAuthority;
import java.util.Set;

public class User{

    private Long id;
    private String name;
    private String password;
    private Set<GrantedAuthority> authorities;


    public User(){

    }

    public User(Long id, String name, String password, Set<GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
