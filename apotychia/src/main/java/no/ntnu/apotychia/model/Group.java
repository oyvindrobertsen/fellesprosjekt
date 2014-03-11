package no.ntnu.apotychia.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peteraa on 3/11/14.
 */
public class Group {
    private final String ID;
    private List<User> members;
    private String name;

    public void addMember(User user){
        members.add(user);
    }

    public void removeMember(User user){
        members.remove(user);
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

    public Group(String ID){
        this.ID = ID;
        members = new ArrayList<User>();
    }
}
