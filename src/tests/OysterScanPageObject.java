package tests;

import com.oyster.OysterCard;
import com.oyster.OysterCardReader;
import com.tfl.billing.*;
import com.tfl.underground.Station;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class OysterScanPageObject {

    private OysterCardForTesting oysterCardForTesting;
    private OysterCard myCard;

    public OysterScanPageObject(){
        oysterCardForTesting = new OysterCardForTesting();
        myCard = new OysterCard();
    }


    public void createOysterCardAndRegisterCardReaders() {
        oysterCardForTesting.connectCardReaders();
        myCard = oysterCardForTesting.getMyCard();
    }

    public void JourneyTouchAssertion() {


        oysterCardForTesting.getStartStationOysterReader().touch(myCard);
        List<JourneyEvent> eventLog = oysterCardForTesting.getCardReadersData().getEventLog();

        JourneyEvent expectedJourneyStart = new JourneyEvent(myCard.id(),oysterCardForTesting.getStartStationOysterReader().id(),true);
        Assert.assertTrue(eventLog.get(0).cardId().equals(expectedJourneyStart.cardId()));
        Assert.assertTrue(eventLog.get(0).readerId().equals(expectedJourneyStart.readerId()));


        Set<UUID> currentlyTravelling = oysterCardForTesting.getCardReadersData().getCurrentlyTravelling();

        Assert.assertTrue(currentlyTravelling.contains(myCard.id()));

        oysterCardForTesting.getEndStationOysterReader().touch(myCard);
        JourneyEvent expectedJourneyEnd = new JourneyEvent(myCard.id(),oysterCardForTesting.getEndStationOysterReader().id(),false);

        Assert.assertTrue(eventLog.get(1).cardId().equals(expectedJourneyEnd.cardId()));
        Assert.assertTrue(eventLog.get(1).readerId().equals(expectedJourneyEnd.readerId()));    }
}
