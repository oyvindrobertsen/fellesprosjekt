package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.repository.EventRepository;
import no.ntnu.apotychia.service.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    GroupRepository groupRepository;

    public Event findEventById(long eventId) {
        Event event = eventRepository.findById(eventId);
        event.setAttending(eventRepository.findAttendingByEventId(eventId));
//        for (Participant p : event.getAttending()) {
//            if (p instanceof Group) {
//                ((Group) p).addAllMembers(groupRepository.findMembers(((Group) p).getId()));
//            }
//        }
        return event;
    }

    public Set<Participant> findAttendingForEventByEventId(long eventId) {
        Set<Participant> participants =  eventRepository.findAttendingByEventId(eventId);
        for (Participant p : participants) {
            if (p instanceof Group) {
                ((Group) p).addAllMembers(groupRepository.findMembers(((Group) p).getId()));
            }
        }
        return participants;
    }

    public List<Event> findAttendingEventsForUserByUsername(String username) {
        return eventRepository.findEventsForUser(username);
    }

    public Long addEvent(Event event) {
        try {
            return eventRepository.insert(event);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addAttending(Long eventId, Participant participant) {
        eventRepository.addAttending(eventId, participant);
    }

    public void deleteEventById(Long id) {
        eventRepository.delete(id);
    }
}
