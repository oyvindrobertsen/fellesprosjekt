package no.ntnu.apotychia.service.repository;

import no.ntnu.apotychia.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EventRepository {

    @Autowired
    JdbcTemplate jt;

    public List<Event> findAll(final String username) {
        List<Event> result = jt.query(
                "SELECT e.* FROM calendarEvent AS e, participants AS p" +
                        "WHERE p.username = ?" +
                        "AND e.eventId = p.eventId",
                new Object[]{username},
                new RowMapper<Event>() {
                    @Override
                    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Event event = new Event(rs.getInt("eventId"));
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

    public void insert(Event event) {
        jt.update(
                "INSERT INTO calendarEvent VALUES (?, ?, ?, ?, ?, ?)",
                event.getEventID(), event.getEventName(), event.getStartTime(),
                event.getEndTime(), event.isActive(), event.getDescription(),
                event.getEventAdmin()
        );
        jt.update(
                "INSERT INTO participants VALUES (?, ?)",
                event.getEventID(), event.getEventAdmin()
        );
    }
}
