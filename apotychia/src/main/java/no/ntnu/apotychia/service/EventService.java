package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Group;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.model.Room;
import no.ntnu.apotychia.service.repository.EventRepository;
import no.ntnu.apotychia.service.repository.GroupRepository;
import no.ntnu.apotychia.service.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Collection;
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
        List<Room> room = eventRepository.findRoomByEventId(eventId);
        if(!room.isEmpty()) {
            event.setRoom(room.get(0));
        } else {
            event.setRoom(null);
        }
        return event;
    }

    public Set<User> findAttendingForEventByEventId(long eventId) {
        Set<User> users = eventRepository.findAttendingByEventId(eventId);
        return users;
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

    public void addAttending(Long eventId, User user) {
        eventRepository.addAttending(eventId, user);
    }

    public void deleteEventById(Long id) {
        eventRepository.delete(id);
    }

    public void addInvited(long eventId, Participant participant) {
        eventRepository.addInvited(eventId, participant);
    }

    public Set<Participant> findInvitedByEventId(Long id) {
        Set<Participant> invitedUsers = eventRepository.findInvitedUsersByEventId(id);
        Set<Participant> invitedGroups = eventRepository.findInvitedGroupsByEventId(id);
        for (Participant participant : invitedGroups) {
            for (User user : groupRepository.findMembers(((Group)participant).getId())) {
                if (!invitedUsers.contains(user)) {
                    invitedUsers.add(user);
                }
            }
        }
        return invitedUsers;
    }

    public Set<User> findDeclinedByEventId(Long id) {
        return eventRepository.findDeclinedByEventId(id);
    }


    public List<Event> findInvitedEventsForUserByUsername(String username) {
        return eventRepository.findInvitedTo(username);
    }

    public void removeInvitedByUsername(Long eventId, String username) {
        eventRepository.removeInvited(eventId, username);
    }

    public void addDeclined(Long id, User currentUser) {
        eventRepository.addDeclined(id, currentUser);
    }

    public void addRoom(Long eventId, Room room){
        eventRepository.addRoom(eventId, room.getRoomNumber());
    }
}
