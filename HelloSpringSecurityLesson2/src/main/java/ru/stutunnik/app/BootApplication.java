package ru.stutunnik.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.stutunnik.app.service.MyUserDetailsService;
import ru.stutunnik.app.service.CustomAuthProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SpringBootApplication
@EnableWebSecurity
@Controller
public class BootApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthProvider customAuthProvider;


    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.authenticationProvider(customAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()
                .antMatchers("/greet/hi/**").hasRole("ADMIN")  // requests login and permits hi access to 'ADMIN' roles
                //.antMatchers("/greet/hi/**").authenticated() // requests login
                .antMatchers("/greet/**").permitAll() // allows unlogined users to access all 'greet' uris
                .anyRequest().authenticated()
                //.antMatchers("/**").hasRole("ADMIN")  //HAS TO BE ABOVE .anyRequest().authenticated()!!!
                .and()
                .formLogin().and()
                .httpBasic();
    }

    //Logout endpoint
    // {http://websystique.com/spring-security/spring-security-4-logout-example/}
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
        //You can redirect wherever you want, but generally it's a good practice to show login screen again.
    }

}
