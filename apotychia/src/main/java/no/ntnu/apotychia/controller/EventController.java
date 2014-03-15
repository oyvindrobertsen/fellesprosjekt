package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable long eventId) {
        return new ResponseEntity<Event>(eventService.findEventById(eventId), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Event addEvent(@ModelAttribute Event event, ModelMap model) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUsername().equals(event.getEventAdmin())) {
            long eventId = eventService.addEvent(event);
            return eventService.findEventById(eventId);
        } else {
            throw new IllegalArgumentException("EventAdmin not same as logged in user");
        }
    }
}
