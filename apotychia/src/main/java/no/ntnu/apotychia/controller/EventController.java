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

import java.sql.Date;
import java.text.SimpleDateFormat;
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
        return new ResponseEntity<List<Event>>(eventService.findAttendingEventsForUserByUsername(currentUser.getUsername()),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{username}")
    public ResponseEntity<List<Event>> getAllEventsForUserByUserName(@PathVariable String username) {
        return new ResponseEntity<List<Event>>(eventService.findAttendingEventsForUserByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable long eventId) {
        return new ResponseEntity<Event>(eventService.findEventById(eventId), HttpStatus.OK);
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