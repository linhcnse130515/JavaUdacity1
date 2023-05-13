package org.project1.service;

import org.project1.model.Customer;

import java.util.Collection;

public interface CustomerService {
    void addCustomer(String email, String firstName, String lastName);
    Customer getCustomer(String customerEmail);
    Collection<Customer> getAllCustomers();
}
