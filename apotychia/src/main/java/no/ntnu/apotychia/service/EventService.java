package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
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
        event.setAttending(eventRepository.findParticipantsByEventId(eventId));
        for (Participant p : event.getAttending()) {
            if (p instanceof Group) {
                ((Group) p).addAllMembers(groupRepository.findMembers(((Group) p).getId()));
            }
        }
        return event;
    }

    public Set<Participant> findParticipantsForEventByEventId(long eventId) {
        Set<Participant> participants =  eventRepository.findParticipantsByEventId(eventId);
        for (Participant p : participants) {
            if (p instanceof Group) {
                ((Group) p).addAllMembers(groupRepository.findMembers(((Group) p).getId()));
            }
        }
        return participants;
    }

    public List<Event> findAllEventsForUserByUsername(String username) {
        return eventRepository.findAll(username);
    }

    public Long addEvent(Event event) {
        try {
            Long eventId = eventRepository.insert(event);
            for (Participant participant : event.getInvited()) {
                eventRepository.addInvited(eventId, participant);
            }
            return eventId;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
