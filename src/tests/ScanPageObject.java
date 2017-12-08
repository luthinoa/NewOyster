package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import org.junit.Assert;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ScanPageObject {

    /*
    This page object will assert that after every touch or card reader, a journey event in generated in the eventLog.
     */

    private OysterCardForTesting oysterCardForTesting;
    private OysterCard myCard;

    public ScanPageObject(){
        oysterCardForTesting = new OysterCardForTesting("Noa Luthi");
        myCard = new OysterCard();
    }


    public void createOysterCardAndRegisterCardReaders() {
        oysterCardForTesting.createCustomerAndConnectCardReaders();
        myCard = oysterCardForTesting.getMyCard();
    }

    public void JourneyTouchAssertion() {

        //first touch (start station)
        OysterCardReader oysterCardReaderStart = oysterCardForTesting.getStartStationOysterReader();
        oysterCardReaderStart.touch(myCard);

        //get eventLog
        List<JourneyEvent> eventLog = oysterCardForTesting.getCardReadersData().getEventLog();

        //create JourneyEvent with the same cardId and readerId as the one we used with touch().
        JourneyEvent expectedJourneyStart = new JourneyEvent(myCard.id(),oysterCardForTesting.getStartStationOysterReader().id(),true);

        //assert that the expected start JourneyEvent (same cardID and readerID) is now in the eventLog.
        UUID cardIdOfStartJourneyEventInEventLog = eventLog.get(0).cardId();
        UUID readerIdOfStartJourneyEventInEventLog = eventLog.get(0).readerId();

        Assert.assertTrue(cardIdOfStartJourneyEventInEventLog.equals(expectedJourneyStart.cardId()));
        Assert.assertTrue(readerIdOfStartJourneyEventInEventLog.equals(expectedJourneyStart.readerId()));

        //get the currently travelling set from GetCardReadersData object.
        Set<UUID> currentlyTravelling = oysterCardForTesting.getCardReadersData().getCurrentlyTravelling();

        //assert that the list contains the cardId after the first touch.
        Assert.assertTrue(currentlyTravelling.contains(myCard.id()));

        //second touch (end station)
        oysterCardForTesting.getEndStationOysterReader().touch(myCard);
        JourneyEvent expectedJourneyEnd = new JourneyEvent(myCard.id(),oysterCardForTesting.getEndStationOysterReader().id(),false);

        //assert that the expected end JourneyEvent (same cardID and readerID) is now in the eventLog.
        UUID cardIdOfEndJourneyEventInEventLog = eventLog.get(1).cardId();
        UUID readerIdOfEndJourneyEventInEventLog = eventLog.get(1).readerId();

        Assert.assertTrue(cardIdOfEndJourneyEventInEventLog.equals(expectedJourneyEnd.cardId()));
        Assert.assertTrue(readerIdOfEndJourneyEventInEventLog.equals(expectedJourneyEnd.readerId()));

        //assert that cardID was removed from currentlyTravelling set.
        Assert.assertTrue((currentlyTravelling.size()==0));
    }
}
