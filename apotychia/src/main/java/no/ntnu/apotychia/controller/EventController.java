package no.ntnu.apotychia.controller;

import no.ntnu.apotychia.model.Event;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/api/event")
public class EventController {

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<Event>();
        Event test = new Event(counter.incrementAndGet());
        test.setEventName("TestEvent");
        eventList.add(test);
        return eventList;
    }
}
