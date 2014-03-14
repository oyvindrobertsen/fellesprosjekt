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

import java.sql.Date;
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

    @Before
    public void before() {
        testUser = new User("testUser");
        testUser.setPasswordAndEncode("testPassword");
        testUser.setFirstName("TestFirstName");
        testUser.setLastName("TestLastName");
        testUser.setEmail("TestEmail@test.com");
        userService.addNewUser(testUser);
    }

    @Test
    public void thatEventsAreAdded() {
        Event testEvent = new Event();
        testEvent.setEventName("TestEvent");
        testEvent.setStartTime(new Date(1394873100000L));
        testEvent.setEndTime(new Date(1394876700000L));
        testEvent.setActive(true);
        testEvent.setEventAdmin(testUser.getUsername());
        testEvent.setDescription("This is a test event");
        HashSet<Participant> participants = new HashSet<Participant>();
        participants.add(testUser);
        testEvent.setParticipants(participants);
        eventService.addEvent(testEvent);
        assertEquals(1, eventService.findAllEventsForUserByUsername(testUser.getUsername()).size());
    }
}
