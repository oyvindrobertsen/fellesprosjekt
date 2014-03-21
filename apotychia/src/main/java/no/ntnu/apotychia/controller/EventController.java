package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.model.Room;
import no.ntnu.apotychia.service.EventService;
import no.ntnu.apotychia.service.UserService;
import no.ntnu.apotychia.service.RoomService;
import no.ntnu.apotychia.service.MailService;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Controller
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Event>> getAttendingEventsForLoggedInUser() {
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
        ret.addAll(eventService.findInvitedEventsForUserByUsername(currentUser.getUsername()));
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

    @RequestMapping(method = RequestMethod.GET, value="/{id}/attending")
    public ResponseEntity<Set<User>> getAttendingForEvent(@PathVariable Long id) {
        return new ResponseEntity<Set<User>>(eventService.findAttendingForEventByEventId(id),
                HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/invited")
    public ResponseEntity<Set<Participant>> getInvitedForEvent(@PathVariable Long id) {
        return new ResponseEntity<Set<Participant>>(eventService.findInvitedByEventId(id), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/declined")
    public ResponseEntity<Set<User>> getDeclinedForEvent(@PathVariable Long id) {
        return new ResponseEntity<Set<User>>(eventService.findDeclinedByEventId(id), HttpStatus.OK);
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
            if (!((User)participant).getUsername().equals(currentUser.getUsername())) {
                eventService.addInvited(eventId, participant);
            }
        }
        eventService.addRoom(eventId, event.getRoom());
        mailService.push(event.getInvited(),
                "You have been invited to a new Event <br> <a href='http://localhost:8080/#/event/edit/" + eventId + "'>New Event</a>");
        return eventService.findEventById(eventId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/attend")
    @ResponseStatus(value = HttpStatus.OK)
    public void attendEvent(@PathVariable Long id) {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        eventService.removeInvitedByUsername(id, currentUser.getUsername());
        eventService.addAttending(id, currentUser);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/decline")
    @ResponseStatus(value = HttpStatus.OK)
    public void declineEvent(@PathVariable Long id) {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        eventService.removeInvitedByUsername(id, currentUser.getUsername());
        eventService.addDeclined(id, currentUser);
    }

    @RequestMapping(method = RequestMethod.GET, value="/invites")
    public ResponseEntity<List<Event>> getInvitedToEventsForLoggedInUser() {
        ApotychiaUserDetails apotychiaUserDetails =
                (ApotychiaUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = userService.findByUsername(apotychiaUserDetails.getUsername());
        List<Event> ret = eventService.findInvitedEventsForUserByUsername(currentUser.getUsername());
        return new ResponseEntity<List<Event>>(ret, HttpStatus.OK);
    }


    /*
    TODO: Add code for endpoints returning events for current/any week, for adding participants to events and notifications.
     */
}