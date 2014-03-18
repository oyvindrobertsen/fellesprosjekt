package no.ntnu.apotychia.service.repository;

import com.mysql.jdbc.Statement;
import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GroupRepository {

    @Autowired
    JdbcTemplate jt;

    public Group findGroup(Long groupId) {
        Group result = jt.queryForObject(
                "SELECT * FROM eventGroup " +
                        "WHERE groupId = ?",
                new Object[]{groupId},
                new RowMapper<Group>() {
                    @Override
                    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Group group = new Group();
                        group.setId(rs.getLong("groupId"));
                        group.setName(rs.getString("groupName"));
                        return group;
                    }
                }
        );
        return result;
    }

    public Set<User> findMembers(Long groupId) {
        List<User> result = jt.query(
                "SELECT p.* FROM person p, memberOf mo " +
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
        return new HashSet<User>(result);
    }

    public Long insert(final Group group) throws SQLException {
        final String sql = "INSERT INTO eventGroup (groupName) VALUES (?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jt.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, group.getName());
                return ps;
            }
        }, holder);
        if (holder.getKey() != null) {
            return holder.getKey().longValue();
        } else {
            throw new SQLException("Error adding group");
        }
    }

    public void addUser(User user, Long groupId) {
        jt.update(
                "INSERT INTO memberOf VALUES (?, ?)",
                new Object[]{groupId, user.getUsername()}
        );
    }

    public List<Group> findAllGroups() {
        List<Group> result = jt.query(
                "SELECT * FROM eventGroup",
                new RowMapper<Group>() {
                    @Override
                    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Group group = new Group();
                        group.setId(rs.getLong("groupId"));
                        group.setName(rs.getString("groupName"));
                        return group;
                    }
                }
        );
        return result;
    }
}
