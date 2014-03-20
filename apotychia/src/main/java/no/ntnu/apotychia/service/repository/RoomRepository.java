package no.ntnu.apotychia.service.repository;

import com.mysql.jdbc.Statement;
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
public class RoomRepository {

    @Autowired
    JdbcTemplate jt;

    public List<Room> findAllRooms() {
        List<Room> result = jt.query(
            "SELECT * FROM room",
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
