package com.tfl.billing;

import com.tfl.external.Customer;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

public class TravelTracker {

    /*
    This class operates as a main controller.
    It is in charge of translating the customers' eventLog list (a list of JourneyEvents of all customers),
    translate it into a JourneyEvent list for each customer, and then a final Journey list for each customer.
    It will charge all customers.
     */

    //this variable is for testing use only.
    private BigDecimal costForTesting;
    private GetCardReadersData getCardReadersData;

    /*
    When creating the travelTracker object, we are initializing the getCardReaderData object,
    which holds the eventLog for all customer.
     */
    public TravelTracker(GetCardReadersData getCardReadersData) {
        this.getCardReadersData = getCardReadersData;
    }

    /*
    Trigger the process of charging all customers journeys.
     */
    public void chargeAccounts() throws InvocationTargetException, IllegalAccessException {

        ExternalJarAdapter externalJarAdapter = new ExternalJarAdapter();

        //get customers from database
        List<Customer> customers = externalJarAdapter.getCustomers();

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
            BigDecimal cost = customerChargeCalculation.chargeJourneys();

            //for testing - if current customer is the is the first customer, assign the cost value the the costForTesting.
            if(i==0) {
                costForTesting = cost;
            }

            //pass total charge to print charge value for each customer.
            externalJarAdapter.charge(customers.get(i), customersJourneys, cost);
        }
    }

    //get the first customer's charging value - for testing.
    public BigDecimal getCostForTesting() {return costForTesting;}
}
