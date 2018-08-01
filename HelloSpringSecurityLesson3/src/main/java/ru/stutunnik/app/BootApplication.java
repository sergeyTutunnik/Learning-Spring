package ru.stutunnik.app;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.stutunnik.app.model.UserRepository;
import ru.stutunnik.app.service.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@SpringBootApplication
@EnableWebSecurity
@Controller
public class BootApplication extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        //auth.authenticationProvider(customAuthProvider);
        auth.authenticationProvider(authenticationProvider()); //Setting provider (configured below)
    }

    //Configuring DataSource Bean
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                //"jbdc:mysql://localhost:3306/auth_db",
                "jdbc:mysql://localhost:3306/auth_db?serverTimezone=UTC",
                "root",
                "12345678"
        );
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        return dataSource;
    }

    //Injecting DataSource dependency into JdbcDAOSupport class via child (UserRepository)
    @Bean
    public UserRepository userRepository() {

        UserRepository userRepository = new UserRepository();
        userRepository.setDataSource(dataSource());
        return userRepository;
    }

    //Configuring DAO AuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Setting UserDetailsService that returns user data
        authProvider.setPasswordEncoder(encoder()); // Setting PasswordEncoder(required). Note that password should be stored encoded
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http

                .authorizeRequests()
                .antMatchers("/greet/hi/**").hasAuthority("admin")  // requests login and permits hi access to 'ADMIN' roles
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
