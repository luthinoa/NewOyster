package com.tfl.billing;

import com.tfl.external.Customer;
import com.tfl.external.CustomerDatabase;
import com.tfl.external.PaymentsSystem;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class TravelTracker {

    private List<Customer> customers;
    private GetCardReadersData getCardReadersData;
    private ExternalJarAdapter externalJarAdapter;
    private BigDecimal cost;
    private BigDecimal costForTesting;

    public TravelTracker(GetCardReadersData getCardReadersData) {
        this.getCardReadersData = getCardReadersData;
        externalJarAdapter = new ExternalJarAdapter();
    }

    /*
    Trigger the process of charging all customers journeys.
     */
    public void chargeAccounts() throws InvocationTargetException, IllegalAccessException {

        //get customers from database
        customers = externalJarAdapter.getCustomers();

        //get the eventLog of all journeys.
        List<JourneyEvent> eventLog = getCardReadersData.getEventLog();

        /*
        For each customer:
        Create a journey event list, then create a journey list, and then charge those journeys.
         */
        for (int i=0; i<customers.size(); i++) {

            //create journey events list for each customer
            CustomerJourneyEvents customerJourneyEvents = new CustomerJourneyEvents(customers.get(i), eventLog);
            List<JourneyEvent> customersJourneyEvents = customerJourneyEvents.createCustomerJourneyEventList();

            //pass journey events to journeys object to generate journey list
            Journeys journeys = new Journeys(customersJourneyEvents);
            List<Journey> customersJourneys = journeys.createJourneysListForCustomer();

            //calculate the customer's charge of all his journeys.
            CustomerChargeCalculation customerChargeCalculation = new CustomerChargeCalculation(customersJourneys);
            cost = customerChargeCalculation.chargeJourneys();

            if(i==0) {
                costForTesting = cost;
            }

            externalJarAdapter.charge(customers.get(i), customersJourneys, cost);
        }
    }

    public BigDecimal getCost() {return costForTesting;}
}
