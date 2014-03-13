package no.ntnu.apotychia.service.repository;

import no.ntnu.apotychia.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepository {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jt;

    public User findOne(String username) {
        logger.info("Looking up user: " + username);
        User result = jt.queryForObject("SELECT * FROM person WHERE username = ?",
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
        if (result != null) {
            logger.info("Found user: " + result.getUsername());
        }
        return result;
    }

    public void insert(User user) {
        logger.info("Inserting user:" + user.getUsername());
        jt.update(
                "INSERT INTO person VALUES (?, ?, ?, ?, ?)",
                new Object[] {user.getUsername(), user.getEncodedPassword(), user.getFirstName(),
                user.getLastName(), user.getEmail()}
        );
    }
}