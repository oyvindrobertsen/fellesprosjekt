package no.ntnu.apotychia.service.repository;

import com.mysql.jdbc.Statement;
import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Participant;
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
import java.util.List;

@Repository
public class EventRepository {

    @Autowired
    JdbcTemplate jt;

    public List<Event> findAll(final String username) {
        List<Event> result = jt.query(
                "SELECT e.* FROM calendarEvent AS e, participants AS p " +
                        "WHERE p.username = ? " +
                        "AND p.eventId = e.eventId",
                new Object[]{username},
                new RowMapper<Event>() {
                    @Override
                    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Event event = new Event();
                        event.setEventID(rs.getLong("eventId"));
                        event.setEventName(rs.getString("eventName"));
                        event.setStartTime(rs.getDate("startTime"));
                        event.setEndTime(rs.getDate("endTime"));
                        event.setActive(rs.getBoolean("isActive"));
                        event.setDescription(rs.getString("description"));
                        event.setEventAdmin(rs.getString("eventAdmin"));
                        return event;
                    }
                }
        );
        return result;
    }

    public void insert(final Event event) {
        final String sql = "INSERT INTO calendarEvent (eventName, startTime, endTime, isActive, description, eventAdmin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jt.update(new PreparedStatementCreator() {
                      @Override
                      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                          PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                          ps.setString(1, event.getEventName());
                          ps.setDate(2, event.getStartTime());
                          ps.setDate(3, event.getEndTime());
                          ps.setBoolean(4, event.isActive());
                          ps.setString(5, event.getDescription());
                          ps.setString(6, event.getEventAdmin());
                          return ps;
                      }
                  }, holder);
        for (Participant p: event.getParticipants()) {
            if (p instanceof User) {
                User u = (User)p;
                jt.update(
                        "INSERT INTO participants (eventId, username) VALUES (?, ?)",
                        holder.getKey().longValue(), u.getUsername()
                );
            } /* Add code for insterting all members of a group here */
        }
    }
}
