package org.project1.service.impl;


import org.project1.model.*;
import org.project1.resource.AdminResource;
import org.project1.service.AdminService;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.project1.util.NumberUtils.isNumber;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Scanner scanner = new Scanner(System.in);

    private final AdminResource adminResource;

    public AdminServiceImpl(AdminResource adminResource) {
        this.adminResource = adminResource;
    }

    /**
     * Prints admin menu to the console.
     */
    @Override
    public void printMenu() {
        System.out.println("Admin menu of Vanya's Hotel Reservation App");
        System.out.println("----------------------------------------");
        System.out.println("1. See all Customers");
        System.out.println("2. See all Rooms");
        System.out.println("3. See all Reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Back to Main Menu");
        System.out.println("----------------------------------------");
        System.out.println("Select a menu option");
    }

    /**
     * Gets all customers using admin resource and if any present, prints them to the console.
     */
    @Override
    public void showAllCustomers() {
        Collection<Customer> allCustomers = adminResource.getAllCustomers();
        if (allCustomers.isEmpty()) {
            System.out.println("There are no registered customers yet. You can add one in main menu");
            return;
        }
        for (Customer aCustomer : allCustomers) {
            System.out.println(aCustomer);
        }
    }

    /**
     * Gets all rooms using admin resource and if any present, prints them to the console.
     */
    @Override
    public void showAllRooms() {
        Collection<IRoom> allRooms = adminResource.getAllRooms();
        if (allRooms.isEmpty()) {
            System.out.println("There are no rooms yet. Please add some");
            return;
        }
        for (IRoom aRoom : allRooms) {
            System.out.println(aRoom);
        }
    }

    /**
     * Gets all reservations using admin resource and if any present, prints them to the console.
     */
    @Override
    public void showAllReservations() {
        Set<Reservation> allReservations = adminResource.getAllReservations();
        if (allReservations.isEmpty()) {
            System.out.println("There are still no reservations");
            return;
        }
        for (Reservation reservation : allReservations) {
            System.out.println(reservation);
        }
    }

    /**
     * Creates multiple or a single new room with properties from administrator's input and records it.
     */
    @Override
    public void addARoom() {
        List<IRoom> newRooms = new ArrayList<>();
        boolean keepAddingRooms = true;
        while (keepAddingRooms) {
            String roomNumber = readRoomNumber(newRooms);
            RoomType roomType = readRoomType();
            if (RoomType.FREE.equals(roomType)) {
                newRooms.add(new FreeRoom(roomNumber, roomType));
            } else {
                double roomPrice = readRoomPrice();
                newRooms.add(new Room(roomNumber, roomPrice, roomType));
            }
            keepAddingRooms = readAddingAnotherRoom();
        }
        adminResource.addRoom(newRooms);
        System.out.println("Rooms were successfully added");
    }

    private String readRoomNumber(List<IRoom> newRooms) {
        System.out.println("Enter room number");
        String input = "";
        boolean isBadRoomNumber = true;
        while (isBadRoomNumber) {
            input = scanner.nextLine();
            if (!isNumber(input)) {
                System.out.println("Room number should be an integer number");
                continue;
            }
            if (!isNewRoomNumber(newRooms, input)) {
                System.out.println("You have already added a room with room number " + input);
            } else {
                isBadRoomNumber = false;
            }
        }
        return input;
    }

    private boolean isNewRoomNumber(List<IRoom> newRooms, String roomNumber) {
        for (IRoom aRoom : newRooms) {
            if (aRoom.getRoomNumber().equals(roomNumber)) {
                return false;
            }
        }
        return true;
    }

    private double readRoomPrice() {
        System.out.println("Enter room price");
        boolean isBadRoomPrice = true;
        String input = "";
        while (isBadRoomPrice) {
            input = scanner.nextLine();
            if (!isNumber(input)) {
                System.out.println("Room price should be a decimal number");
                continue;
            }
            isBadRoomPrice = false;
        }
        return Double.parseDouble(input);
    }

    private RoomType readRoomType() {
        System.out.println("Choose room type. \"s\" for single or \"d\" for double or \"f\" for free.");
        RoomType roomType = null;
        boolean isBadRoomType = true;
        while (isBadRoomType) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "d":
                    isBadRoomType = false;
                    roomType = RoomType.DOUBLE;
                    break;
                case "s":
                    isBadRoomType = false;
                    roomType = RoomType.SINGLE;
                    break;
                case "f":
                    isBadRoomType = false;
                    roomType = RoomType.FREE;
                    break;
                default:
                    System.out.println("Enter \"s\" for single or \"d\" " + "for double");
            }
        }
        return roomType;
    }

    private boolean readAddingAnotherRoom() {
        System.out.println("Add another room? (y/n)");
        boolean keepAddingRooms = true;
        boolean isBadInput = true;
        while (isBadInput) {
            String input = scanner.nextLine();
            switch (input.toLowerCase()) {
                case "y":
                    // Restart inner while loop
                    isBadInput = false;
                    break;
                case "n":
                    // Exit both loops
                    isBadInput = false;
                    keepAddingRooms = false;
                    break;
                default: // Keep inside inner loop
                    System.out.println("Enter \"y\" for yes or \"n\" for no");
            }
        }
        return keepAddingRooms;
    }
}
