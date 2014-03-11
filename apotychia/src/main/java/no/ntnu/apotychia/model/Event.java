package no.ntnu.apotychia.model;

import java.sql.Date;
import java.util.Set;

public class Event {
    private final long eventID;
    private String eventName;
    private Date startTime;
    private Date endTime;
    private boolean isActive;
    private User eventAdmin;
    private Set<User> participants;

    public Event(long id) {
        this.eventID = id;
    }

    public long getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public User getEventAdmin() {
        return eventAdmin;
    }

    public void setEventAdmin(User eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<User> participants) {
        this.participants = participants;
    }
}
