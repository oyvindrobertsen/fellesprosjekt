package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;

    @RequestMapping(method = RequestMethod.GET, value="/{username}")
    public ResponseEntity<List<Event>> getAllEventsForUser(@PathVariable String username) {
        return new ResponseEntity<List<Event>>(eventService.findAllEventsForUserByUsername(username), HttpStatus.OK);
    }
}
