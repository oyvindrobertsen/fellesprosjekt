package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.model.Room;
import no.ntnu.apotychia.service.EventService;
import no.ntnu.apotychia.service.UserService;
import no.ntnu.apotychia.service.RoomService;
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

import java.util.*;

@Controller
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAttendingEventsForLoggedInUserForCurrentWeek() {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        List<Event> ret = eventService.findAttendingEventsForUserByUsername(currentUser.getUsername());
//        List<Event> out = new ArrayList<Event>();
//        int week = new GregorianCalendar().getWeekYear();
        for (Event event : ret) {
            if (event.getEventAdmin().equals(currentUser.getUsername())) {
                event.setAdmin(true);
            }
//            GregorianCalendar c = new GregorianCalendar();
//            c.setTime(event.getStartTime());
//            if (c.getWeekYear() == week) {
//                logger.info("weee");
//                out.add(event);
//            }
        }
        Collections.sort(ret);
        return new ResponseEntity<List<Event>>(ret, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/user/{username}")
    public ResponseEntity<List<Event>> getAllEventsForUserByUserName(@PathVariable String username) {
        return new ResponseEntity<List<Event>>(eventService.findAttendingEventsForUserByUsername(username), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        Event event = eventService.findEventById(id);
        if (event.getEventAdmin().equals(currentUser.getUsername())) {
            event.setAdmin(true);
        }
        return new ResponseEntity<Event>(event, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value="/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEventById(id);
    }

    @RequestMapping(method = RequestMethod.GET, value="/{id}/participants")
    public ResponseEntity<Set<Participant>> getAttendingForEvent(@PathVariable long eventId) {
        return new ResponseEntity<Set<Participant>>(eventService.findAttendingForEventByEventId(eventId),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody Event addEvent(@RequestBody Event event, ModelMap model) {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        event.setEventAdmin(currentUser.getUsername());
        long eventId = eventService.addEvent(event);
        eventService.addAttending(eventId, currentUser);
        for (Participant participant : event.getInvited()) {
            eventService.addInvited(eventId, participant);
        }
        eventService.addRoom(eventId, event.getRoom());
        return eventService.findEventById(eventId);
    }

    /*
    TODO: Add code for endpoints returning events for current/any week, for adding participants to events and notifications.
     */
}