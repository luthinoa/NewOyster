package com.tfl.billing;
import com.tfl.external.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerJourneyEvents {

    /*
    This class generates and holds JourneyEvents of one customer only, separating it from the eventLog of all customers.
     */

    private List<JourneyEvent> customerJourneyEvents;
    private Customer customer;
    private List<JourneyEvent> eventLog;

    public CustomerJourneyEvents(Customer customer, List<JourneyEvent> eventLog) {
        customerJourneyEvents = new ArrayList<>();
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
