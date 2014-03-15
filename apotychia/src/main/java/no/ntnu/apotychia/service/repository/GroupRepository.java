package no.ntnu.apotychia.service.repository;

import no.ntnu.apotychia.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GroupRepository {

    @Autowired
    JdbcTemplate jt;

    public List<User> findMembers(Long groupId) {
        List<User> result = jt.query(
                "SELECT p.* FROM oerson p, memberOf mo " +
                        "WHERE mo.groupId = ? " +
                        "AND mo.username = p.username",
                new Object[]{groupId},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User(rs.getString("username"));
                        user.setEmail(rs.getString("mail"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        return user;
                    }
                }
        );
        return result;
    }

}
