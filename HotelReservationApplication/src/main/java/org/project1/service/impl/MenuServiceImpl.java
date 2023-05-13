package org.project1.service.impl;

import org.project1.resource.HotelResource;
import org.project1.model.IRoom;
import org.project1.model.Reservation;
import org.project1.service.MenuService;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import static org.project1.util.NumberUtils.isNumber;

@Service
public class MenuServiceImpl implements MenuService {
    private static final Date date = new Date();
    private final HotelResource hotelResource;
    private static final DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    private static final Scanner scanner = new Scanner(System.in);

    public MenuServiceImpl(HotelResource hotelResource) {
        this.hotelResource = hotelResource;
    }

    @Override
    public void printMenu() {
        System.out.println("Welcome to ChenLin's Hotel Reservation App");
        System.out.println("----------------------------------------");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("----------------------------------------");
        System.out.println("Please enter a number to select a menu option");
    }

    /**
     * Gets all recorded reservations for the customer identified by the email from user's input. Also, checks that
     * a user is registered with the provided email.
     */
    @Override
    public void showCustomersReservations() {
        // Read customer's email
        System.out.println("Please enter your email");
        String email = readEmail();

        // Check that customer is registered
        if (!customerAlreadyExists(email)) {
            System.out.println("You are still not registered with this email. Please create an account");
        }

        // Get customer's reservations
        Collection<Reservation> customerReservations = hotelResource.getCustomersReservations(email);

        // Display customer's reservations
        if (customerReservations.isEmpty()) {
            System.out.println("You still have no reservations with us");
        } else {
            System.out.println("Your reservations:");
            for (Reservation aReservation : customerReservations) {
                System.out.println(aReservation);
            }
        }
    }

    private String readEmail() {
        boolean keepReadingEmail = true;
        String email = "";
        while (keepReadingEmail) {
            String input = scanner.nextLine();
            // Validate email
            if (!isValidEmail(input)) {
                System.out.println("It is not a valid email. Please enter like example@mail.com");
                continue;
            }
            email = input;
            keepReadingEmail = false;
        }

        return email;
    }

    private boolean isValidEmail(String input) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(input).matches();
    }

    private boolean customerAlreadyExists(String email) {
        return hotelResource.getCustomer(email) != null;
    }

    /**
     * Records a new customer with data from user's input. Also, checks that the provided email doesn't belong to
     * already registered customer.
     */
    @Override
    public void createNewAccount() {
        boolean keepAddingNewAccount = true;
        while (keepAddingNewAccount) {

            System.out.println("Enter your email");
            String email = readEmail();

            // Check that customer with this email already exists
            if (customerAlreadyExists(email)) {
                System.out.println("Customer with this email already registered.");
                continue;
            }

            System.out.println("Enter your first name");
            String firstName = readName(true);

            System.out.println("Enter your last name");
            String lastName = readName(false);

            // Stop outer loop
            keepAddingNewAccount = false;

            // Add new account
            hotelResource.createACustomer(email, firstName, lastName);
            System.out.println("Your account successfully created");
        }
    }

    private String readName(boolean isFirstName) {
        String name = "";
        String input;
        String nameType = "last";
        if (isFirstName) {
            nameType = "first";
        }
        boolean keepReadingName = true;
        while (keepReadingName) {
            input = scanner.nextLine();
            if (!hasCharacters(input)) {
                System.out.println("Your " + nameType + " name should have at least one letter.");
                continue;
            }

            name = input;
            keepReadingName = false;
        }

        return name;
    }

    private boolean hasCharacters(String input) {
        return input.matches(".*[a-zA-Z]+.*");
    }

    /**
     * Searches available rooms for the dates from user's input and if one found, books it for the customer. If no
     * rooms found for the provided dates, searches rooms for the next seven days. While booking also checks that
     * customer with the email from the input is already registered.
     */
    @Override
    public void findAndReserveARoom() throws Exception {
        boolean keepFindingAndReservingARoom = true;
        while (keepFindingAndReservingARoom) {

            // Read check-in date
            System.out.println("Enter check-in date in format mm/dd/yyyy. Example: 05/13/2023");
            Date checkIn = readDate();

            // Read check-out date
            System.out.println("Enter check-out date in format mm/dd/yyyy Example: 05/13/2023");
            Date checkOut = readDate();

            // Check that check-in is before check-out
            if (checkIn.after(checkOut)) {
                System.out.println("Your check-in date is later than checkout date. Please reenter dates");
                continue;
            }

            // Find available rooms
            Collection<IRoom> availableRooms = findAvailableRooms(checkIn, checkOut);
            if (availableRooms.isEmpty()) {
                // Redirect back to main menu
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Print available rooms for initial dates
            System.out.println("Following rooms are available for booking: ");
            for (IRoom aRoom : availableRooms) {
                System.out.println(aRoom);
            }

            // Ask if customer wants to book a room
            if (stopBooking()) {
                // Go to main menu
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Ask if customer has an account
            if (customerHasNoAccount()) {
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Read customer's email
            System.out.println("Please enter your email");
            String email = readEmail();

            // Check that customer is registered
            if (!customerAlreadyExists(email)) {
                System.out.println("You are still not registered with this email. Please create an account");
                keepFindingAndReservingARoom = false;
                continue;
            }

            // Read which room to book
            String roomNumberToBook = readRoomNumberToBook(availableRooms);

            // Book a room
            IRoom roomObjectToBook = hotelResource.getRoom(roomNumberToBook);
            Reservation newReservation = hotelResource.bookARoom(email, roomObjectToBook,
                    checkIn, checkOut);

            // Print reservation
            System.out.println(newReservation);

            // Redirect back to main menu
            keepFindingAndReservingARoom = false;
        }
    }

    private Date readDate() {
        boolean keepReadingDate = true;
        Date date = null;
        while (keepReadingDate) {
            String input = scanner.nextLine();
            if (isValidDate(input)) {
                try {
                    simpleDateFormat.setLenient(false);
                    date = simpleDateFormat.parse(input);
                } catch (ParseException ex) {
                    System.out.println("Try entering the date again");
                    continue;
                }
                if (!date.before(MenuServiceImpl.date)) {
                    keepReadingDate = false;
                } else {
                    System.out.println("This date is in the past. Please reenter the date");
                }
            } else {
                System.out.println("Renter the date in format mm/dd/yyyy");
            }
        }
        return date;
    }

    /**
     * Implementation inspired by example on www.baeldung.com
     */
    private boolean isValidDate(String input) {
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(input);
        } catch (ParseException ex) {
            System.out.println(ex.getLocalizedMessage());
            return false;
        }

        return true;
    }

    private Collection<IRoom> findAvailableRooms(Date checkIn, Date checkOut) {
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn,
                checkOut);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms found for selected dates. Trying to find a room in the next 7 days");

            // Shift dates
            checkIn = shiftDate(checkIn);
            checkOut = shiftDate(checkOut);

            // Find rooms available for shifted dates
            Collection<IRoom> incomingRooms = hotelResource.findARoom(checkIn, checkOut);

            if (incomingRooms.isEmpty()) {
                System.out.println("No free rooms in the next 7 days found. Try different dates");
            } else {
                // Print shifted dates and available rooms
                Date minAvailableDay = hotelResource.findMinAvailableDay();
                if (minAvailableDay.before(checkIn)) {
                    checkOut = new Date(checkOut.getTime() - checkIn.getTime() + minAvailableDay.getTime());
                    checkIn = minAvailableDay;
                }
                System.out.println("You can book following rooms from " + checkIn + " to " + checkOut + ":");
                for (IRoom aRoom : incomingRooms) {
                    System.out.println(aRoom);
                }
            }
        }
        return availableRooms;
    }

    private Date shiftDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 7);
        return cal.getTime();
    }

    private boolean stopBooking() {
        System.out.println("Would you like to book one of the rooms above? (y/n)");
        boolean stopBooking = false;
        boolean keepReadingAnswer = true;
        while (keepReadingAnswer) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y":
                    // Proceed with booking
                    keepReadingAnswer = false;
                    break;
                case "n":
                    keepReadingAnswer = false;
                    stopBooking = true;
                    break;
                default: // Keep asking
                    System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return stopBooking;
    }

    private boolean customerHasNoAccount() {
        System.out.println("Do you have an account? (y/n)");
        boolean customerHasNoAccount = false;
        boolean keepReadingAnswer = true;
        while (keepReadingAnswer) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y":
                    // Proceed with booking
                    keepReadingAnswer = false;
                    break;
                case "n":
                    // Go to main menu
                    System.out.println("Please create an account in main menu");
                    customerHasNoAccount = true;
                    keepReadingAnswer = false;
                    break;
                default: // Keep asking
                    System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return customerHasNoAccount;
    }

    private String readRoomNumberToBook(Collection<IRoom> availableRooms) {
        System.out.println("Please enter which room to book. (Room number)");
        String roomNumberToBook = "";
        boolean keepReadingRoomNumber = true;
        while (keepReadingRoomNumber) {
            String input = scanner.nextLine();
            if (isNumber(input)) {
                // Check that the room is available for booking
                boolean isAvailableRoom = false;
                for (IRoom aRoom : availableRooms) {
                    if (aRoom.getRoomNumber().equals(input)) {
                        isAvailableRoom = true;
                        break;
                    }
                }
                if (isAvailableRoom) {
                    keepReadingRoomNumber = false;
                    roomNumberToBook = input;
                } else {
                    System.out.println("The room you picked is actually not available. Please enter a room number " +
                            "from the the list above");
                }
            } else {
                System.out.println("Room number should be an integer number");
            }
        }
        return roomNumberToBook;
    }
}
