package org.project1.service;

import org.project1.model.Customer;
import org.project1.model.IRoom;
import org.project1.model.Reservation;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public interface ReservationService {
    boolean addRoom(IRoom room);
    IRoom getARoom(String roomId) throws Exception;
    Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate);
    Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate);
    Collection<Reservation> getCustomersReservation(Customer customer);
    Set<IRoom> getRooms();
    Set<Reservation> getAllReservations();
    Date findMinAvailableDay();
}
