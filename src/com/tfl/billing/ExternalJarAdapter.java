package com.tfl.billing;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;
import com.tfl.underground.OysterReaderLocator;
import com.tfl.underground.Station;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExternalJarAdapter implements ExternalJar {

    /*
    This adapter moderates the data that is held in the external jar file, to match the objects we generated ourselves.
     */


    List<Customer> customersInDatabase;

    public ExternalJarAdapter(){
        customersInDatabase = CustomerDatabase.getInstance().getCustomers();
    }


    //add a customer to the database
    @Override
    public void addCustomer(String nameOfCustomer) {

        //Create a random uuid for oyster card
        UUID oysterCardID = UUID.randomUUID();
        OysterCard oysterCard = new OysterCard(oysterCardID.toString());
        Customer customer = new Customer(nameOfCustomer,oysterCard);

        //If the card id which was randomly generated already exists, randomly generate a new id until ok.
        while(customersInDatabase.contains(customer)) {
            oysterCardID = UUID.randomUUID();
            oysterCard = new OysterCard(oysterCardID.toString());
            customer = new Customer(nameOfCustomer,oysterCard);
        }

        boolean flag = false;

        /*
        Checking that customer is not in the database already.
         */
        for ( Customer thisCustomer: customersInDatabase) {
            if(thisCustomer.fullName().equals(nameOfCustomer)) {
                flag = true;
            }
        }

        //If customer is not in the database, add it to the database.
        if(!flag) {
            customersInDatabase.add(customer);

        /*Since add function is randomly adding customer to database, after every call to "add" - if
        customer is not in data base, call "add" until he is added properly.
        */
            while(!customersInDatabase.contains(customer)) {
                customersInDatabase.add(customer);
            }
        }
    }

    /*
    Iterate through the customers in data base and return customer object with the same full name/
     */
    @Override
    public Customer getCustomer(String nameOfCustomer) {

        for(Customer customer:customersInDatabase) {
            if(customer.fullName().equals(nameOfCustomer)) {
                return customer;
            }
        }
        return null;
    }


    @Override
    public OysterCardReader getCardReader(Station station) {

        OysterCardReader ourReader = OysterReaderLocator.atStation(station);

        return ourReader;
    }

    @Override
    public OysterCard getOysterCard(String nameOfCustomer) {

        for(Customer customer:customersInDatabase) {
            if(customer.fullName().equals(nameOfCustomer)) {
                customer.cardId();
                OysterCard myOysterCard = new OysterCard(customer.cardId().toString());
                return myOysterCard;
            }
        }

        System.out.println("no such customer in database sorry");
        return null;
    }

    @Override
    public List<Customer> getCustomersInDatabase() {
        return customersInDatabase;
    }

    @Override
    public void charge(Customer customer,List<Journey> journeys, BigDecimal cost) {
        PaymentsSystem.getInstance().charge(customer,journeys,cost);
    }
}
