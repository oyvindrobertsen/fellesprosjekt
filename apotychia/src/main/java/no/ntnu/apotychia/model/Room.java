package no.ntnu.apotychia.model;

public class Room{
    private final String roomNumber;
    private final int capacity;

    public int getCapacity(){
        return capacity;
    }

    public String getRoomNumber(){
        return roomNumber;
    }

    public Room(String roomNumber, int capacity){
        this.roomNumber = roomNumber;
        this.capacity = capacity;
    }





}