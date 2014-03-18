package no.ntnu.apotychia.model;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.sql.Date;
import java.util.Set;
import javax.persistence.*;

public class Event {
    private long eventId;
    private String eventName;
    private Date startTime;
    private Date endTime;
    private boolean isActive;
    private String description;
    private String eventAdmin;
    private Set<Participant> invited;
    private Set<Participant> attending;

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
        return getEventName();
    }
}
