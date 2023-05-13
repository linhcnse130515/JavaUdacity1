package org.project1.resource;

import org.project1.model.Customer;
import org.project1.model.IRoom;
import org.project1.model.Reservation;
import org.project1.service.CustomerService;
import org.project1.service.ReservationService;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * API which serves as intermediary between the admin UI and the services.
 *
 *  @author Chu Nguyen Linh
 */
@Component
public class AdminResource {
    private final CustomerService customerService;
    private final ReservationService reservationService;

    /**
     * Constructor of this class.
     *
     * @param customerService       customerService object that handles {@link Customer}s
     * @param reservationService    reservationService object that handles {@link IRoom}s and
     *                              {@link Reservation}s
     */
    public AdminResource(CustomerService customerService,
                         ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    /**
     * Calls a service to record a collection of new rooms.
     *
     * @param rooms list of new rooms to record
     */
    public void addRoom(List<IRoom> rooms) {

        for (IRoom newRoom: rooms) {
            if (!reservationService.addRoom(newRoom)) {
                System.out.println("You have already added a room with room number " + newRoom.getRoomNumber());
            };
        }
    }

    /**
     * Calls a service to get all recorded rooms.
     *
     * @return collection of all recorded rooms
     */
    public Collection<IRoom> getAllRooms() {

        Set<IRoom> allRooms = reservationService.getRooms();
        return new ArrayList<>(allRooms);
    }

    /**
     * Calls a service to get all customers.
     *
     * @return  collection of all recorded customers
     */
    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    /**
     * Calls a service to get all recorded reservations
     *
     * @return  set of all reservations
     */
    public Set<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }
}
