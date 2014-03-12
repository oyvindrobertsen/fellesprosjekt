package no.ntnu.apotychia.service.repository;

import no.ntnu.apotychia.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jt;

    public User findOne(String username) {
        User result = jt.queryForObject("SELECT * FROM PERSON WHERE username = ?",
                new Object[] {username},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User(rs.getString("username"));
                        user.setEmail(rs.getString("mail"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setPassword(rs.getString("pwd"));
                        return user;
                    }
                }
        );
        return result;
    }
}