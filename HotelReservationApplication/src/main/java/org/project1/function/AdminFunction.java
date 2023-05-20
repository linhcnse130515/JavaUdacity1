package org.project1.function;


import org.project1.service.AdminService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AdminFunction {
    private final AdminService adminService;

    public AdminFunction(AdminService adminService) {
        this.adminService = adminService;
    }

    public void open() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                adminService.printMenu();
                int input = Integer.parseInt(ApplicationFunction.scanner.nextLine());
                switch (input) {
                    case 1:
                        adminService.showAllCustomers();
                        break;
                    case 2:
                        adminService.showAllRooms();
                        break;
                    case 3:
                        adminService.showAllReservations();
                        break;
                    case 4:
                        adminService.addARoom();
                        break;
                    case 5:
                        System.out.println("Returning to the main menu");
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Please enter a number representing a menu option from above");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a number");
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            } catch (Exception ex) {
                System.out.println("Unknown error occurred.");
                System.out.println(ex.getLocalizedMessage());
            }
        }
    }

}
