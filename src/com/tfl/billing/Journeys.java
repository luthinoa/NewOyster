package com.tfl.billing;

import java.util.*;

public class Journeys {

    private List<Journey> journeys = new ArrayList<Journey>();
    private List<JourneyEvent> customerJourneyEvents;

    public Journeys(List<JourneyEvent> customerJourneyEvents) {
        this.customerJourneyEvents = customerJourneyEvents;
    }

    /*
    iterate through the customer's journey events and determine the start and end of a journey,
    adding Journey objects to the Journeys list.
     */
    public List<Journey> createJourneysListForCustomer() {

        JourneyEvent start = null;

        for (JourneyEvent event : customerJourneyEvents) {
            if (event instanceof JourneyStart) {
                start = event;
            }
            if (event instanceof JourneyEnd && start != null) {
                journeys.add(new Journey(start, event));
                start = null;
            }
        }
        return journeys;
    }

    public List<Journey> getCustomersJourneys() {
        return journeys;
    }
}

