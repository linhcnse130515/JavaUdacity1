package org.project1.function;


import org.project1.service.MenuService;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class ApplicationFunction {

    private final AdminFunction adminFunction;
    private final MenuService menuService;
    private static final Scanner scanner = new Scanner(System.in);

    public ApplicationFunction(AdminFunction adminFunction, MenuService menuService) {
        this.adminFunction = adminFunction;
        this.menuService = menuService;
    }

    public void open() {
        boolean keepRunning = true;
        while (keepRunning) {
            try {
                menuService.printMenu();
                int input = Integer.parseInt(scanner.nextLine());
                switch (input) {
                    case 1:
                        menuService.findAndReserveARoom();
                        break;
                    case 2:
                        menuService.showCustomersReservations();
                        break;
                    case 3:
                        menuService.createNewAccount();
                        break;
                    case 4:
                        goToAdminMenu();
                        break;
                    case 5: {
                        System.out.println("Exiting the app");
                        keepRunning = false;
                        scanner.close();
                        break;
                    }
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

    private void goToAdminMenu() {
        adminFunction.open();
    }
}
