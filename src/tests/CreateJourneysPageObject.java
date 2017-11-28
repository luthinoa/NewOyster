package tests;

import com.oyster.OysterCard;
import com.tfl.billing.*;
import com.tfl.external.Customer;
import org.junit.Assert;

import java.util.List;
import java.util.UUID;

public class CreateJourneysPageObject {

    /*
    This page object, will create a false journey, and assert every step from the "touch", and to the final list of Journey".
     */

    private OysterCardForTesting oysterCardForTesting;
    private OysterCard myCard;
    private List<JourneyEvent> eventLog;
    private List<JourneyEvent> customersJourneyEventsList;
    private List<Journey> customersJourneys;

    public CreateJourneysPageObject(){
        oysterCardForTesting = new OysterCardForTesting();
    }

    public void createEventLog() {

        //connect the card readers for testing, held in the oysterCardForTesting object.
        oysterCardForTesting.connectCardReaders();

        //get the oyster card held the the oysterCardForTesting object.
        myCard = oysterCardForTesting.getMyCard();

        //create a false journey by touching twice at beginning and end of journey.
        oysterCardForTesting.getStartStationOysterReader().touch(myCard);
        oysterCardForTesting.getEndStationOysterReader().touch(myCard);
    }

    public void assertJourneyEventsForOneCustomer() {

        /*
        assert that the eventLog of one customers was generated properly, and holds the two journeyEvents we created by touching
        our false readers twice, and no other journeyEvents of other customers.
         */

        eventLog = oysterCardForTesting.getCardReadersData().getEventLog();

        /*
        add a false customer with a false journey event, and add in to eventLog of all customers.
         */
        Customer customer = new Customer("Fred Bloggs",myCard);
        OysterCard oysterCard = new OysterCard("3f1b3b55-f266-4426-ba1b-bcc506541866");
        UUID falseOysterCardID = oysterCard.id();

        //we set the false readerId of the falseJourneyEvent to the same reader as the testing journey.
        UUID falseOysterCardReaderID = oysterCardForTesting.getStartStationOysterReader().id();

        //add false journeyEvent to eventLog.
        JourneyEvent falseJourneyEvent = new JourneyEvent(falseOysterCardID,falseOysterCardReaderID,true);
        eventLog.add(falseJourneyEvent);

        //create our customer's JourneyEvent list from the eventLog.
        CustomerJourneyEvents customerJourneyEvents = new CustomerJourneyEvents(customer, eventLog);
        customersJourneyEventsList = customerJourneyEvents.createCustomerJourneyEventList();

        /*
            Assert that our customer's JourneyEvent list contains only his journeyEvents, and not other false customer's journeyEvents.
            Do that by asserting that the first two JourneyEvents from the eventLog, are the only objects in the customersJourneyEventList.
            That would mean that the third false JourneyEvent was not added.
         */

        //compare cardIds of first JourneyEvent in the customerJourneyEvent and EventLog.
        UUID startJourneyEventCardIdFromCustomerJourneyEvents = customersJourneyEventsList.get(0).cardId();
        UUID startJourneyEventCardIdFromEventLog = eventLog.get(0).cardId();
        Assert.assertTrue(startJourneyEventCardIdFromCustomerJourneyEvents.equals(startJourneyEventCardIdFromEventLog));

        //compare readerIDs of first JourneyEvent in the customerJourneyEvent and EventLog.
        UUID startJourneyEventReaderIdFromCustomerJourneyEvents = customersJourneyEventsList.get(0).readerId();
        UUID startJourneyEventReaderIdFromEventLog = eventLog.get(0).readerId();
        Assert.assertTrue(startJourneyEventReaderIdFromCustomerJourneyEvents.equals(startJourneyEventReaderIdFromEventLog));

        //compare cardIDs of second JourneyEvent in the customerJourneyEvent and EventLog.
        UUID endJourneyEventCardIdFromCustomerJourneyEvents = customersJourneyEventsList.get(1).cardId();
        UUID endJourneyEventCardIdFromEventLog = eventLog.get(1).cardId();
        Assert.assertTrue(endJourneyEventCardIdFromCustomerJourneyEvents.equals(endJourneyEventCardIdFromEventLog));

        //compare reader IDs of second JourneyEvent in the customerJourneyEvent and EventLog.
        UUID endJourneyEventReaderIdFromCustomerJourneyEvents = customersJourneyEventsList.get(1).readerId();
        UUID endJourneyEventReaderIdFromEventLog = eventLog.get(1).readerId();
        Assert.assertTrue(endJourneyEventReaderIdFromCustomerJourneyEvents.equals(endJourneyEventReaderIdFromEventLog));

        //assert that list size is 2, hence holds only our journey events.
        Assert.assertTrue(customersJourneyEventsList.size()==2);
    }

    public void assertJourneysForOneCustomer(){

        /*
        This method will assert that creating a journeys list from a journeyEvent list of our customer is done properly.
         */

        //call generating the journey list from our customerJourneyEventsList
        Journeys journeys = new Journeys(customersJourneyEventsList);
        customersJourneys = journeys.createJourneysListForCustomer();

        /*
        Assert that the Journey created from the customerJourneyEventList, contains our two JourneyEvents as his start and end.
         */

        Journey journeyInCustomersJourneys = customersJourneys.get(0);
        UUID cardIdOfStartJourneyEventInJourney = journeyInCustomersJourneys.getStart().cardId();
        UUID readerIdOfStartJourneyEventInJourney = journeyInCustomersJourneys.getStart().readerId();
        UUID cardIdOfEndJourneyEventInJourney = journeyInCustomersJourneys.getEnd().cardId();
        UUID readerIdOfEndJourneyEventInJourney = journeyInCustomersJourneys.getEnd().readerId();

        //assert Journey contains our start JourneyEvent
        Assert.assertTrue(cardIdOfStartJourneyEventInJourney.equals(customersJourneyEventsList.get(0).cardId()));
        Assert.assertTrue(readerIdOfStartJourneyEventInJourney.equals(customersJourneyEventsList.get(0).readerId()));

        //assert Journey contains our end JourneyEvent.
        Assert.assertTrue(cardIdOfEndJourneyEventInJourney.equals(customersJourneyEventsList.get(1).cardId()));
        Assert.assertTrue(readerIdOfEndJourneyEventInJourney.equals(customersJourneyEventsList.get(1).readerId()));
    }
}
