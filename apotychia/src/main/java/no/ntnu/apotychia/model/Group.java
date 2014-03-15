package no.ntnu.apotychia.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Group implements Participant {
    private long groupId;
    private List<User> members;
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

    public List getAllMembers(){
        return this.members;
    }

    public boolean contains(User user){
        return members.contains(user);
    }
}
