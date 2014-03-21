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
import java.util.*;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    GroupRepository groupRepository;

    public Event findEventById(long eventId) {
        Event event = eventRepository.findById(eventId);
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

    public void updateEventById(Event event){
        eventRepository.updateEvent(event);
    }

    public void addInvited(long eventId, Participant participant) {
        eventRepository.addInvited(eventId, participant);
    }

    public Set<User> findInvitedByEventId(Long id) {
        Set<User> invitedUsers = eventRepository.findInvitedUsersByEventId(id);
        Set<Group> invitedGroups = eventRepository.findInvitedGroupsByEventId(id);
        for (Group participant : invitedGroups) {
            for (User user : groupRepository.findMembers(((Group)participant).getId())) {
                if (!invitedUsers.contains(user) && !user.getUsername().equals(eventRepository.findById(id).getEventAdmin())) {
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

    public void removeAttendingByUsername(Long eventId, String username){
        eventRepository.removeAttendingUser(eventId, username);
    }

    public void removeInvitedByUsername(Long eventId, String username) {
        eventRepository.removeInvitedUser(eventId, username);
    }
    public void removeInvitedByGroupID(long eventId, long groupId) {
        eventRepository.removeInvitedGroup(eventId, groupId);
    }

    public void addDeclined(Long id, User currentUser) {
        eventRepository.addDeclined(id, currentUser);
    }

    public void deleteAttendingByEventId(Long id){
        eventRepository.deleteAttendingByEventId(id);
    }

    public void deleteInvitedByEventId(Long id) {
        eventRepository.deleteInvitedByEventId(id);
    }

    public List<Event> findGroupInvitesForUser(String username) {
        List<Group> groups = groupRepository.findGroupsForUser(username);
        List<Event> groupInvites = new ArrayList<Event>();
        for (Group group : groups) {
            groupInvites.addAll(eventRepository.findInvitationsForGroupByGroupId(group.getId()));
        }
        Set<Event> groupInvitesSet = new HashSet<Event>(groupInvites);
        Set<Event> ret = new HashSet<Event>();
        List<Event> userAttending = eventRepository.findEventsForUser(username);
        for (Event event : groupInvitesSet) {
            if (!userAttending.contains(event)) {
                ret.add(event);
            }
        }
        return new ArrayList<Event>(ret);
    }
}
