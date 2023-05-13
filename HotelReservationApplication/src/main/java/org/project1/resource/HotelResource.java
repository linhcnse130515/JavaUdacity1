package org.project1.resource;

import org.project1.model.Customer;
import org.project1.model.IRoom;
import org.project1.model.Reservation;
import org.project1.service.CustomerService;
import org.project1.service.ReservationService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * API which serves as intermediary between the customer UI and the services.
 *  @author Chu Nguyen Linh
 */
@Component
public class HotelResource {
    private final CustomerService customerService;
    private final ReservationService reservationService;

    /**
     * Constructor of this class.
     *
     * @param customerService       customerService object that handles {@link Customer}s
     * @param reservationService    reservationService object that handles {@link IRoom}s and
     *                              {@link Reservation}s
     */
    public HotelResource(CustomerService customerService,
                         ReservationService reservationService) {
        this.customerService = customerService;
        this.reservationService = reservationService;
    }

    /**
     * Calls a service to get a customer with the supplied email.
     *
     * @param email string with email of the required customer
     * @return      customer object which has the supplied email
     */
    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    /**
     * Calls a service to record a new {@link Customer} with supplied data.
     *
     * @param email     string with customer's email
     * @param firstName string with customer's first name
     * @param lasName   string with customer's last name
     */
    public void createACustomer(String email, String firstName, String lasName) {
        customerService.addCustomer(email, firstName, lasName);
    }

    /**
     * Calls a service to get a room with the provided room number.
     *
     * @param roomNumber    string with the number of the required room
     * @return              iRoom object of the room with the supplied number
     */
    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    /**
     * Calls a service to get a {@link Customer} with the supplied email and reserve a room for the acquired
     * customer.
     *
     * @param customerEmail string with the email of the customer booking a room
     * @param room          iRoom object of the room to book
     * @param checkInDate   date object of check-in
     * @param checkOutDate  date object of check-out
     * @return              reservation object as the result of booking
     */
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate,
                                 Date checkOutDate) {

        Customer customer = getCustomer(customerEmail);
        return reservationService.reserveARoom(customer, room, checkInDate,
                checkOutDate);
    }

    /**
     * Calls a service to get a {@link Customer} with the supplied email and get all their reservations.
     *
     * @param customerEmail string with the email of the customer booking a room
     * @return              collection of reservations recorded for the customer with supplied email
     */
    public Collection<Reservation> getCustomersReservations(String customerEmail) {

        Customer customer = getCustomer(customerEmail);
        return reservationService.getCustomersReservation(customer);
    }

    /**
     * Calls a service to find rooms available for booking for the supplied dates.
     *
     * @param checkIn   date object of check-in
     * @param checkOut  date object of check-out
     * @return          collection of rooms available for booking
     */
    public Collection<IRoom> findARoom(Date checkIn, Date checkOut) {
        return reservationService.findRooms(checkIn, checkOut);
    }
}
