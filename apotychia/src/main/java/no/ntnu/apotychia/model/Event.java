package no.ntnu.apotychia.model;

import com.fasterxml.jackson.annotation.JsonRootName;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.*;


public class Event implements Comparable<Event> {
    private long eventId;
    private String eventName;
    private Timestamp startTime;
    private Timestamp endTime;
    private boolean isActive;
    private String description;
    private String eventAdmin;
    private String location;
    private Room roomId;
    private Set<Participant> invited;
    private Set<User> attending;
    private boolean isAdmin;
    private Room room;

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

    public Set<User> getAttending() {
        return attending;
    }

    public void setAttending(Set<User> attending) {
        this.attending = attending;
    }

    public Set<Participant> getInvited() {
        return invited;
    }

    public void setInvited(Set<Participant> invited) {
        this.invited = invited;
    }

    @Override
    public String toString() {
        return getEventName() + ", " + getStartTime() + ", " + getEndTime();
    }

    @Override
    public int compareTo(Event o) {
        return getStartTime().compareTo(o.getStartTime());
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Room getRoom(){
        return room;
    }

    public String getLocation() {
        return (location != null) ? location : "";
    }
    public void setLocation(String location) {
        this.location = location;
    }
}
    
