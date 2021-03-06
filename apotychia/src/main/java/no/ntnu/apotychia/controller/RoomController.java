package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Room;
import no.ntnu.apotychia.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Room>> getAllRooms() {
        return new ResponseEntity<List<Room>>(roomService.getAllRooms(), HttpStatus.OK);
    }
}

