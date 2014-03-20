package no.ntnu.apotychia.model;

public class Room{
    private long roomNumber;
    private long capacity;

    public void setRoomNr(long roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getCapacity(){
        return capacity;
    }

    public long getRoomNumber(){
        return roomNumber;
    }

}