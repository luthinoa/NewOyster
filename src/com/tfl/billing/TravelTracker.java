package com.tfl.billing;

import com.oyster.*;
import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class TravelTracker {

    private List<Customer> customers;
    private GetCardReadersData getCardReadersData;

    public TravelTracker(GetCardReadersData getCardReadersData) {

        this.getCardReadersData = getCardReadersData;
        this.customers = new ArrayList<Customer>();
    }

    /*
    Get a list of all customer in our database
     */
    public void getCustomersFromDatabase() {
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance();
        customers = customerDatabase.getCustomers();
    }

    /*
    Trigger the process of charging all customers journeys.
     */
    public void chargeAccounts() throws InvocationTargetException, IllegalAccessException {

        getCustomersFromDatabase();

        //get the eventLog of all journeys.
        List<JourneyEvent> eventLog = getCardReadersData.getEventLog();

        /*
        For each customer:
        Create a journey event list, then create a journey list, and then charge those journeys.
         */
        for(Customer customer:customers) {

            //create journey events list for each customer
            CustomerJourneyEvents customerJourneyEvents = new CustomerJourneyEvents(customer,eventLog);
            List<JourneyEvent> myJourneyEvents = customerJourneyEvents.createCustomerJourneyEventList();

            //pass journey events to journeys object to generate journey list
            Journeys journeys = new Journeys(myJourneyEvents);
            List<Journey> myJourneys = journeys.createJourneysListForCustomer();

            //calculate the customer's charge of all his journeys.
            CustomerChargeCalculation customerChargeCalculation = new CustomerChargeCalculation(myJourneys);
            BigDecimal charge = customerChargeCalculation.chargeJourneys();
            PaymentsSystem.getInstance().charge(customer, myJourneys, charge);        }
    }
}
