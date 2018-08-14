package ru.stutunnik.app.model;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private BCryptPasswordEncoder encoder; // Stored passwords should be encoded

    public void createUser(User user) {

        // TODO: 02.08.18 Inside one transaction or Hibernate

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userName", user.getName());
        params.addValue("userPassword", encoder.encode(user.getPassword()));
        params.addValue("userAuth", user.getAuthorities().iterator().next().getAuthority());

        int result = this.getNamedParameterJdbcTemplate().update("insert into USERS values (null, :userName, :userPassword);", params);

        int newUserId = this.getNamedParameterJdbcTemplate().queryForObject("select id from users where user_name = :userName", params, Integer.class);
        int roleId = this.getNamedParameterJdbcTemplate().queryForObject("select role_id from roles where role_sysname = :userAuth", params, Integer.class);

        params.addValue("newUserId", newUserId);
        params.addValue("roleId", roleId);

        int resultRole = this.getNamedParameterJdbcTemplate().update("insert into USER_ROLES values (null, :roleId, :newUserId)", params);
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
