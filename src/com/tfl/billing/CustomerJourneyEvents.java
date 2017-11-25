package com.tfl.billing;
import com.tfl.external.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerJourneyEvents {

    private List<JourneyEvent> customerJourneyEvents = new ArrayList<JourneyEvent>();
    private Customer customer;
    private List<JourneyEvent> eventLog;

    public CustomerJourneyEvents(Customer customer, List<JourneyEvent> eventLog) {
        this.customer = customer;
        this.eventLog = eventLog;
    }

    /*
    Iterate through the event log and create a journey event list of only the current customer.
     */
    public List<JourneyEvent> createCustomerJourneyEventList() {

        for (JourneyEvent journeyEvent : eventLog) {
            if (journeyEvent.cardId().equals(customer.cardId())) {
                customerJourneyEvents.add(journeyEvent);
            }
        }
        return customerJourneyEvents;
    }
}
