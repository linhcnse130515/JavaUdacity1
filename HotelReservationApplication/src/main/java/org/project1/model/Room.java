package org.project1.model;

import lombok.Setter;

@Setter
public class Room implements IRoom {
    protected String roomNumber;
    protected Double price;
    protected RoomType roomType;

    public Room(String roomNumber, Double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomNumber='" + roomNumber + '\'' +
                ", price=" + price +
                ", roomType=" + roomType +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room otherRoom = (Room) o;
        return (roomNumber == null && otherRoom.getRoomNumber() == null) ||
                ((otherRoom.getRoomNumber().equals(roomNumber)) && otherRoom.getRoomType().equals(roomType));
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (roomNumber != null) {
            result = 31 * result + roomNumber.hashCode();
        }
        return result;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return price;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return false;
    }
}
