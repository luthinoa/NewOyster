package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;

public interface ExternalJar {

    /*
    This interface will be used to implement an adapter for the data held in the external jar file.
     */

    void addCustomer(String customerName);

    OysterCardReader getCardReader(Station station);

    void charge(Customer customer, List<Journey> journeys, BigDecimal cost);

    OysterCard getOysterCard(String nameOfCustomer);

    Customer getCustomer(String nameOfCustomer);

    List<Customer> getCustomersInDatabase();
}
