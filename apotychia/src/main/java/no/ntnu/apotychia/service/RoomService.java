package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.Room;
import no.ntnu.apotychia.service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        List<Room> rooms = roomRepository.findAllRooms();
        return rooms;
    }
}