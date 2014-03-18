package no.ntnu.apotychia.event;

import no.ntnu.apotychia.Application;
import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.model.Participant;
import no.ntnu.apotychia.model.User;
import no.ntnu.apotychia.service.EventService;
import no.ntnu.apotychia.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.HashSet;
import static org.junit.Assert.*;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class EventServiceTest {

    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;
    private User testUser;
    Event testEvent;

    @Before
    public void before() {
        testUser = new User("testUser");
        testUser.setPasswordAndEncode("testPassword");
        testUser.setFirstName("TestFirstName");
        testUser.setLastName("TestLastName");
        testUser.setEmail("TestEmail@test.com");
        userService.addNewUser(testUser);
        testEvent = new Event();
        testEvent.setEventName("TestEvent");
        testEvent.setStartTime(new Timestamp(1394873100000L));
        testEvent.setEndTime(new Timestamp(1394876700000L));
        testEvent.setActive(true);
        testEvent.setEventAdmin(testUser.getUsername());
        testEvent.setDescription("This is a test event");
    }

    @Test
    public void thatEventsAreAdded() {
        Long eventId = eventService.addEvent(testEvent);
        eventService.addAttending(eventId, testUser);
        assertEquals(1, eventService.findAttendingEventsForUserByUsername(testUser.getUsername()).size());
    }

    @Test
    public void thatParticipantsAreAddedAndCanBeRetrieved() {
        long eventId = eventService.addEvent(testEvent);
        eventService.addAttending(eventId, testUser);
        assertEquals(1, eventService.findAttendingForEventByEventId(eventId).size());
    }
}
