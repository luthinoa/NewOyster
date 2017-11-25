package tests;

import com.oyster.OysterCard;
import com.tfl.billing.*;
import com.tfl.external.Customer;
import com.tfl.underground.Station;
import org.junit.Assert;


import java.util.ArrayList;
import java.util.List;

public class CreateJourneysPageObject {

    private OysterCardForTesting oysterCardForTesting;
    private OysterCard myCard;
    private List<JourneyEvent> eventLog;

    public void createEventLog() {

        oysterCardForTesting = new OysterCardForTesting("38400000-8cf0-11bd-b23e-10b96e4ef00d", Station.PADDINGTON,Station.BAKER_STREET);
        myCard = oysterCardForTesting.getMyCard();
        oysterCardForTesting.connectCardReaders();

        oysterCardForTesting.getStartStationOysterReader().touch(myCard);
        oysterCardForTesting.getEndStationOysterReader().touch(myCard);
    }

    public void assertJourneyEventsForOneCustomer(){

        eventLog = oysterCardForTesting.getCardReadersData().getEventLog();

        Customer customer = new Customer("Fred Bloggs",myCard);

        OysterCard oysterCard = new OysterCard("3f1b3b55-f266-4426-ba1b-bcc506541866");
        JourneyEvent falseJourneyEvent = new JourneyStart(oysterCard.id(),oysterCardForTesting.getStartStationOysterReader().id());
        eventLog.add(falseJourneyEvent);

        CustomerJourneyEvents customerJourneyEvents = new CustomerJourneyEvents(customer, eventLog);
        List<JourneyEvent> customersJourneyEvents = customerJourneyEvents.createCustomerJourneyEventList();

        Assert.assertTrue(customersJourneyEvents.contains(eventLog.get(0));
        Assert.assertTrue(customersJourneyEvents.contains(eventLog.get(1));
        Assert.assertTrue(customersJourneyEvents.size()==2);
    }

    public void assertJourneysForOneCustomer(){
        Journeys journeys = new Journeys(customersJourneyEvents);
        List<Journey> customersJourneys = journeys.createJourneysListForCustomer();


    }



}
