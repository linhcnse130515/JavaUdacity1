package org.project1.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "customer=" + customer +
                ", room=" + room +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        // Compare rooms
        boolean roomsEquals = (room == null && this.getRoom() == null) ||
                (room != null && room.equals(this.getRoom()));
        // Compare check-in
        boolean checkInEquals = (checkInDate == null && this.getCheckInDate() == null)
                || (checkInDate != null && checkInDate.equals(this.getCheckInDate()));
        // Compare check-out
        boolean checkOutEquals = (checkOutDate == null && this.getCheckOutDate() == null)
                || (checkOutDate != null && checkOutDate.equals(this.getCheckOutDate()));

        return roomsEquals && checkInEquals && checkOutEquals;
    }

    @Override
    public final int hashCode() {
        int result = 17;
        if (room != null) {
            result = 31 * result + room.hashCode();
        }
        if (checkInDate != null) {
            result = 31 * result + checkInDate.hashCode();
        }
        if (checkOutDate != null) {
            result = 31 * result + checkOutDate.hashCode();
        }
        return result;
    }
}
