package ru.stutunnik.app.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
//Using DAO support class that provides us jdbcTemplate.
// DataSource injection is provided via configuration(annotation or xml).
public class UserRepository extends NamedParameterJdbcDaoSupport {

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

        String sql = "select USERS.*, ROLES.role_sysname from USERS\n" +
                "left join USER_ROLES on USERS.id = USER_ROLES.user_id\n" +
                "join ROLES on USER_ROLES.role_id = ROLES.role_id\n" +
                "where USERS.user_name = :userName";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userName", userName);

        User user = (User) this.getNamedParameterJdbcTemplate().query(sql, params, new UserResultSetExtractor());

        return user;
    }

    //RowMapper is called many times for each row returned
    @Deprecated
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

    //ResultSetExtractor is called once.
    private final static class UserResultSetExtractor implements ResultSetExtractor {
        @Override
        public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

            User user = new User();
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            // next() moves the cursor forward once (default is 0, first returned row would be 1 )
            // next() returns false if no next row found
            while (resultSet.next()) {

                //isFirst() returns true if row is first
                if (resultSet.isFirst()) {
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("user_name"));
                    user.setPassword(resultSet.getString("user_password"));
                }
                GrantedAuthority role = new SimpleGrantedAuthority(resultSet.getString("role_sysname"));
                grantedAuthorities.add(role);
            }
            user.setAuthorities(grantedAuthorities);
            return user;
        }
    }
}
