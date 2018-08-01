package ru.stutunnik.app.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class UserRepository{


    private DataSource dataSource;

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


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

        String sql = "select * from Users where user_name=:userName";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userName", userName);

        User foundUser = jdbcTemplate.queryForObject(sql, params, new UserRowMapper());

        return foundUser;
    }

    private final static class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {

            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("user_name"));
            user.setPassword(resultSet.getString("user_password"));
            return user;
        }
    }
}
