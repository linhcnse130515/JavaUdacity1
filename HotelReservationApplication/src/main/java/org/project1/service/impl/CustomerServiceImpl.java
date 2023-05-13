package org.project1.service.impl;

import org.project1.model.Customer;
import org.project1.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<String, Customer> customers;

    private CustomerServiceImpl() {
        this.customers = new HashMap<>();
    }

    /**
     * Creates a new {@link Customer} and records it if no customer already recorded with the provided email.
     *
     * @param email     string, email of the customer
     * @param firstName string, first name of the customer
     * @param lastName  string, last name of the customer
     * @throws IllegalArgumentException if a customer with the supplied email was already recorded
     */
    @Override
    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        if (customers.containsKey(email)) {
            throw new IllegalArgumentException("Customer with this email is already registered.");
        } else {
            customers.put(email, newCustomer);
        }
    }

    /**
     * Returns a customer object if a customer with provided email has already been registered.
     *
     * @param customerEmail string, customer's email
     * @return customer identified by provided email
     */
    @Override
    public Customer getCustomer(String customerEmail) {
        if (this.customers.containsKey(customerEmail)) {
            return this.customers.get(customerEmail);
        }
        return null;
    }

    /**
     * Returns all customers registered in the app.
     *
     * @return collection of customers
     */
    @Override
    public Collection<Customer> getAllCustomers() {
        return customers.values();
    }
}
