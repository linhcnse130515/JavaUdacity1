package org.project1.service.impl;

import org.project1.model.Customer;
import org.project1.model.IRoom;
import org.project1.model.Reservation;
import org.project1.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final Set<IRoom> rooms;
    private final Set<Reservation> reservations;

    public ReservationServiceImpl() {
        this.rooms = new HashSet<>();
        reservations = new HashSet<>();
    }

    /**
     * Records the supplied room if a room with the same number was not recorded yet.
     *
     * @param room iRoom, an object of a room to add
     * @throws IllegalArgumentException if a room with the same ID already exists
     */
    @Override
    public boolean addRoom(IRoom room) {
        if (rooms.contains(room)) {
            return false;
        } else {
            rooms.add(room);
            return true;
        }
    }

    /**
     * Returns a room if one was already recorded with the supplied ID.
     *
     * @param roomId string for room's ID
     * @return iRoom corresponding to supplied ID
     * @throws IllegalArgumentException if there is no room with supplied ID
     */
    @Override
    public IRoom getARoom(String roomId) throws Exception {
        try {
            return rooms.stream()
                    .filter(r -> roomId.equals(r.getRoomNumber()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("There is no room with number " + roomId));
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    /**
     * Creates a new reservation and if the same reservation was not recorded yet, records it.
     *
     * @param customer     customer for whom the reservation is made
     * @param room         iRoom which is reserved
     * @param checkInDate  date object of check-in
     * @param checkOutDate date object of check-out
     * @return reservation newly created
     * @throws IllegalArgumentException if the supplied room is already reserved for exactly the same supplied days
     */
    @Override
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation newReservation = new Reservation(customer, room, checkInDate,
                checkOutDate);
        if (reservations.contains(newReservation)) {
            throw new IllegalArgumentException("This room is already reserved for these days");
        }
        reservations.add(newReservation);
        return newReservation;
    }

    /**
     * Finds rooms available for booking withing the supplied dates.
     *
     * @param checkInDate  date object of check-in
     * @param checkOutDate date object of check-out
     * @return collection of rooms available for the supplied dates
     */
    @Override
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // Copy all rooms
        Set<IRoom> availableRooms = new HashSet<>(this.rooms);

        // Compare with dates of existing reservations
        for (Reservation aReservation : this.reservations) {
            boolean checkResult = checkDates(aReservation, checkInDate,
                    checkOutDate);
            if (!checkResult) {
                // Remove the room from the list of available rooms
                availableRooms.remove(aReservation.getRoom());
            }
        }

        return new ArrayList<>(availableRooms);
    }

    /**
     * Finds all reservations for the supplied customer.
     *
     * @param customer customer for whom reservations are searched
     * @return collection for reservations for the supplied customer
     */
    @Override
    public Collection<Reservation> getCustomersReservation(Customer customer) {
        List<Reservation> customersReservations = new ArrayList<>();
        for (Reservation aReservation : this.reservations) {
            if (aReservation.getCustomer().equals(customer)) {
                customersReservations.add(aReservation);
            }
        }

        return customersReservations;
    }

    /**
     * Returns all rooms recorded so far.
     *
     * @return map of rooms
     */
    @Override
    public Set<IRoom> getRooms() {
        return rooms;
    }

    /**
     * Returns all reservations recorded so far.
     *
     * @return map of reservations
     */
    @Override
    public Set<Reservation> getAllReservations() {
        return reservations;
    }

    @Override
    public Date findMinAvailableDay() {
        if (!reservations.isEmpty()) {
            Reservation min = reservations.stream()
                    .min((o1, o2) -> (int) (o1.getCheckOutDate().getTime() - o2.getCheckOutDate().getTime()))
                    .get();
            return min.getCheckOutDate();
        }
        return null;
    }

    /**
     * Checks if the supplied reservation conflicts with the supplied check-in and check-out dates.
     *
     * @param reservation reservation to compare with the supplied dates
     * @param checkIn     date object of check-in
     * @param checkOut    date object of check-out
     * @return datesCheckResult object of a helper class containing a check result for each date
     */
    boolean checkDates(Reservation reservation, Date checkIn, Date checkOut) {
        boolean isCheckInOK = checkIn.before(reservation.getCheckInDate()) ||
                checkIn.compareTo(reservation.getCheckOutDate()) >= 0;
        boolean isCheckOutOK = checkOut.compareTo(reservation.getCheckInDate()) <= 0 ||
                checkOut.after(reservation.getCheckOutDate());
        return isCheckInOK && isCheckOutOK;
    }
}
