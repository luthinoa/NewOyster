package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.List;

public class ExternalJarAdapter implements ExternalJar {

    @Override
    public List<Customer> getCustomers() {

        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        List<Customer> customers = customerDatabase.getCustomers();
        return customers;
    }

    @Override
    public OysterCardReader getCardReader(Station station) {

        OysterCardReader ourReader = OysterReaderLocator.atStation(station);

        return ourReader;
    }

    @Override
    public OysterCard getOysterCard(String id) {

        OysterCard myOysterCard = new OysterCard(id);
        return myOysterCard;
    }

    @Override
    public void charge(Customer customer,List<Journey> journeys, BigDecimal cost) {
        PaymentsSystem.getInstance().charge(customer,journeys,cost);
    }
}
