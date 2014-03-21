package no.ntnu.apotychia.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;


public class Group implements Participant {
    private long groupId;
    private Set<User> members;
    private String name;

    public void addMember(User user){
        members.add(user);
    }

    public void addAllMembers(Collection<User> members) {
        this.members.addAll(members);
    }

    public void removeMember(User user){
        members.remove(user);
    }

    public void setId(long id) {
        this.groupId = id;
    }

    /**
     * Returns a Long-wrapper around the primitive long groupId
     * @return Long Id
     */
    public Long getId() {
        return this.groupId;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public Set<User> getMembers(){
        return this.members;
    }

    public boolean contains(User user){
        return members.contains(user);
    }
}
