package no.ntnu.apotychia.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import javax.persistence.*;

public class Event {
    private long eventId;
    private String eventName;
    private Timestamp startTime;
    private Timestamp endTime;
    private boolean isActive;
    private String description;
    private String eventAdmin;
    private Set<Participant> invited;
    private Set<Participant> attending;
    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public long getEventID() {
        return eventId;
    }

    public void setEventId(long id) {
        this.eventId = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getEventAdmin() {
        return eventAdmin;
    }

    public void setEventAdmin(String eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Participant> getAttending() {
        return attending;
    }

    public void setAttending(Set<Participant> attending) {
        this.attending = attending;
    }

    @Override
    public String toString() {
        return getEventName() + ", " + getStartTime() + ", " + getEndTime();
    }
}
