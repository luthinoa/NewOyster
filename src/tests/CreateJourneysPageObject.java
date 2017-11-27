package tests;

import com.oyster.OysterCard;
import com.tfl.billing.*;
import com.tfl.external.Customer;
import org.junit.Assert;

import java.util.List;

public class CreateJourneysPageObject {

    private OysterCardForTesting oysterCardForTesting;
    private OysterCard myCard;
    private List<JourneyEvent> eventLog;
    private List<JourneyEvent> customersJourneyEventsList;
    private List<Journey> customersJourneys;

    public CreateJourneysPageObject(){
        oysterCardForTesting = new OysterCardForTesting();

    }

    public void createEventLog() {

        oysterCardForTesting.connectCardReaders();
        myCard = oysterCardForTesting.getMyCard();

        oysterCardForTesting.getStartStationOysterReader().touch(myCard);
        oysterCardForTesting.getEndStationOysterReader().touch(myCard);
    }

    public void assertJourneyEventsForOneCustomer(){

        eventLog = oysterCardForTesting.getCardReadersData().getEventLog();

        Customer customer = new Customer("Fred Bloggs",myCard);
        OysterCard oysterCard = new OysterCard("3f1b3b55-f266-4426-ba1b-bcc506541866");

        JourneyEvent falseJourneyEvent = new JourneyEvent(oysterCard.id(),oysterCardForTesting.getStartStationOysterReader().id(),true);
        eventLog.add(falseJourneyEvent);

        CustomerJourneyEvents customerJourneyEvents = new CustomerJourneyEvents(customer, eventLog);
        customersJourneyEventsList = customerJourneyEvents.createCustomerJourneyEventList();

        Assert.assertTrue(customersJourneyEventsList.get(0).cardId().equals(eventLog.get(0).cardId()));
        Assert.assertTrue(customersJourneyEventsList.get(0).readerId().equals(eventLog.get(0).readerId()));
        Assert.assertTrue(customersJourneyEventsList.get(1).cardId().equals(eventLog.get(1).cardId()));
        Assert.assertTrue(customersJourneyEventsList.get(1).readerId().equals(eventLog.get(1).readerId()));
        Assert.assertTrue(customersJourneyEventsList.size()==2);
    }

    public void assertJourneysForOneCustomer(){

        Journeys journeys = new Journeys(customersJourneyEventsList);
        customersJourneys = journeys.createJourneysListForCustomer();

        Assert.assertTrue(customersJourneys.get(0).getStart().cardId().equals(customersJourneyEventsList.get(0).cardId()));
        Assert.assertTrue(customersJourneys.get(0).getStart().readerId().equals(customersJourneyEventsList.get(0).readerId()));

        Assert.assertTrue(customersJourneys.get(0).getEnd().cardId().equals(customersJourneyEventsList.get(1).cardId()));
        Assert.assertTrue(customersJourneys.get(0).getEnd().readerId().equals(customersJourneyEventsList.get(1).readerId()));
    }
}
