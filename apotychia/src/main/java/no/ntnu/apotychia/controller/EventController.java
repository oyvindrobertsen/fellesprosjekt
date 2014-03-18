package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.EventService;
import no.ntnu.apotychia.service.UserService;
import no.ntnu.apotychia.service.security.ApotychiaUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAttendingEventsForLoggedInUser() {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        List<Event> ret = eventService.findAttendingEventsForUserByUsername(currentUser.getUsername());
        for (Event event : ret) {
            if (event.getEventAdmin().equals(currentUser.getUsername())) {
                event.setAdmin(true);
            }
        }
        return new ResponseEntity<List<Event>>(ret, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/user/{username}")
    public ResponseEntity<List<Event>> getAllEventsForUserByUserName(@PathVariable String username) {
        return new ResponseEntity<List<Event>>(eventService.findAttendingEventsForUserByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable long id) {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        Event event = eventService.findEventById(id);
        if (event.getEventAdmin().equals(currentUser.getUsername())) {
            event.setAdmin(true);
        }
        return new ResponseEntity<Event>(event, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}/participants")
    public ResponseEntity<Set<Participant>> getParticipantsForEvent(@PathVariable long eventId) {
        return new ResponseEntity<Set<Participant>>(eventService.findAttendingForEventByEventId(eventId),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Event addEvent(@RequestBody Event event, ModelMap model) {
        logger.info(event.toString());
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        event.setEventAdmin(currentUser.getUsername());
        long eventId = eventService.addEvent(event);
        eventService.addAttending(eventId, currentUser);
        // Add code to invite people
        return eventService.findEventById(eventId);
    }

    /*
    TODO: Add code for endpoints returning events for current/any week, for adding participants to events and notifications.
     */
}