package ru.stutunnik.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.stutunnik.app.model.UserRepository;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfig {

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

}
