package no.ntnu.apotychia.service;

import no.ntnu.apotychia.model.Event;
import no.ntnu.apotychia.service.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public List<Event> findAllEventsForUserByUsername(String username) {
        return eventRepository.findAll(username);
    }

    public void addEvent(Event event) {
        eventRepository.insert(event);
    }
}
