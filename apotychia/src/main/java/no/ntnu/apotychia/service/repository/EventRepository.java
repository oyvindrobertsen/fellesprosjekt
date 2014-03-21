package no.ntnu.apotychia.service.repository;

import com.mysql.jdbc.Statement;
import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.model.Room;
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
public class EventRepository {

    @Autowired
    JdbcTemplate jt;

    public List<Event> findInvitedTo(final String username) {
        List<Event> result = jt.query(
                "SELECT e.* FROM calendarEvent e, invited i " +
                        "WHERE i.username = ? " +
                        "AND i.eventId = e.eventId",
                new Object[]{username},
                new RowMapper<Event>() {
                    @Override
                    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Event event = new Event();
                        event.setEventId(rs.getLong("eventId"));
                        event.setEventName(rs.getString("eventName"));
                        event.setStartTime(rs.getTimestamp("startTime"));
                        event.setEndTime(rs.getTimestamp("endTime"));
                        event.setActive(rs.getBoolean("isActive"));
                        event.setDescription(rs.getString("description"));
                        event.setEventAdmin(rs.getString("eventAdmin"));
                        return event;
                    }
                }
        );
        if(result != null){

            return result;
        }
        else {
            return result;
        }
    }

    public List<Event> findEventsForUser(String username) {
        List<Event> result = jt.query(
                "SELECT e.* FROM calendarEvent e, attending a " +
                        "WHERE a.username = ? " +
                        "AND a.eventId = e.eventId",
                new Object[]{username},
                new RowMapper<Event>() {
                    @Override
                    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Event event = new Event();
                        event.setEventId(rs.getLong("eventId"));
                        event.setEventName(rs.getString("eventName"));
                        event.setStartTime(rs.getTimestamp("startTime"));
                        event.setEndTime(rs.getTimestamp("endTime"));
                        event.setActive(rs.getBoolean("isActive"));
                        event.setDescription(rs.getString("description"));
                        event.setEventAdmin(rs.getString("eventAdmin"));
                        return event;
                    }
                }
        );
        return result;
    }

    public Long insert(final Event event) throws SQLException {
        final String sql = "INSERT INTO calendarEvent (eventName, startTime, endTime, isActive, description, eventAdmin) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jt.update(new PreparedStatementCreator() {
                      @Override
                      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                          PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                          ps.setString(1, event.getEventName());
                          ps.setTimestamp(2, event.getStartTime());
                          ps.setTimestamp(3, event.getEndTime());
                          ps.setBoolean(4, event.isActive());
                          ps.setString(5, event.getDescription());
                          ps.setString(6, event.getEventAdmin());
                          return ps;
                      }
                  }, holder);
        if (holder.getKey() != null) {
            return holder.getKey().longValue();
        } else {
            throw new SQLException("Error adding event");
        }
    }

    public void addInvited(Long eventId, Participant participant) {
        if (participant instanceof User) {
            User u = (User)participant;
            jt.update(
                    "INSERT INTO invited (eventId, username) VALUES (?, ?)",
                    eventId, u.getUsername()
            );
        } else {
            Group g = (Group)participant;
            jt.update(
                    "INSERT INTO invited (eventId, groupId) VALUES (?, ?)",
                    eventId, g.getId()
            );
        }
    }



    public void addAttending(Long eventId, User user) {
        jt.update(
                "INSERT INTO attending (eventId, username) VALUES (?, ?)",
                eventId, user.getUsername()
        );
    }

    public void addDeclined(Long id, User currentUser) {
        jt.update(
                "INSERT INTO declined (eventId, username) VALUES (?, ?)",
                id, currentUser.getUsername()
        );
    }

    public Event findById(long eventId) {
        Event result = jt.queryForObject("SELECT * FROM calendarEvent WHERE eventId = ?",
                new Object[]{eventId},
                new RowMapper<Event>() {
                    @Override
                    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Event event = new Event();
                        event.setEventId(rs.getLong("eventId"));
                        event.setEventName(rs.getString("eventName"));
                        event.setStartTime(rs.getTimestamp("startTime"));
                        event.setEndTime(rs.getTimestamp("endTime"));
                        event.setActive(rs.getBoolean("isActive"));
                        event.setDescription(rs.getString("description"));
                        event.setEventAdmin(rs.getString("eventAdmin"));
                        return event;
                    }
                });
        return result;
    }

    public Set<User> findAttendingByEventId(long eventId) {
        List<User> result = jt.query(
                "SELECT p.* FROM person p, attending a " +
                        "WHERE a.eventId = ? " +
                        "AND p.username = a.username",
                new Object[]{eventId},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setEmail(rs.getString("mail"));
                        return user;
                    }
                }
        );
        return new HashSet<User>(result);
    }

    public void delete(Long id) {
        jt.update("DELETE FROM calendarEvent WHERE eventId = ?", id);
    }

    public Set<Participant> findInvitedUsersByEventId(Long eventId) {
        List<Participant> result = jt.query(
                "SELECT p.* FROM person p, invited i " +
                        "WHERE i.eventId = ? " +
                        "AND p.username = i.username",
                new Object[]{eventId},
                new RowMapper<Participant>() {
                    @Override
                    public Participant mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setEmail(rs.getString("mail"));
                        return user;
                    }
                }
        );
        return new HashSet<Participant>(result);
    }


    public Set<Participant> findInvitedGroupsByEventId(Long id) {
        List<Participant> result = jt.query(
                "SELECT eg.* FROM eventGroup eg, invited i " +
                        "WHERE i.eventId = ? " +
                        "AND eg.groupId = i.groupId",
                new Object[]{id},
                new RowMapper<Participant>() {
                    @Override
                    public Participant mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Group group = new Group();
                        group.setId(rs.getLong("groupId"));
                        group.setName(rs.getString("groupName"));
                        return group;
                    }
                }
        );
        return new HashSet<Participant>(result);
    }

    public void removeInvited(Long eventId, String username) {
        jt.update("DELETE FROM invited " +
                "WHERE username = ? " +
                "AND eventId = ?",
                new Object[]{username, eventId});
    }

    public Set<User> findDeclinedByEventId(Long id) {
        List<User> result = jt.query(
                "SELECT p.* FROM person p, declined d " +
                        "WHERE d.eventId = ? " +
                        "AND p.username = d.username",
                new Object[]{id},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                        User user = new User();
                        user.setUsername(rs.getString("username"));
                        user.setFirstName(rs.getString("firstName"));
                        user.setLastName(rs.getString("lastName"));
                        user.setEmail(rs.getString("mail"));
                        return user;
                    }
                }
        );
        return new HashSet<User>(result);
    }

    public void addRoom(long eventId, long roomNr) {
        jt.update(
            "INSERT INTO booked (eventId, roomNr) VALUES (?, ?)",
            eventId, roomNr
        );
    }

    public List<Room> findRoomByEventId(long eventId) {
        List<Room> result = jt.query(
                "SELECT room.* FROM room, booked " +
                        "WHERE eventId = ? " + 
                        "and booked.roomnr = room.roomnr",

                new Object[]{eventId},
                new RowMapper<Room>() {
                    @Override
                    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Room room = new Room();
                        room.setRoomNr(rs.getLong("roomNr"));
                        room.setCapacity(rs.getLong("capacity"));
                        return room;
                    }
                }
            );
        return result;
    }
}