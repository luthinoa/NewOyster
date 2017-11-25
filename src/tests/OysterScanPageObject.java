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


    public void createOysterCardAndRegisterCardReaders() {

        oysterCardForTesting = new OysterCardForTesting("38400000-8cf0-11bd-b23e-10b96e4ef00d",Station.PADDINGTON,Station.BAKER_STREET);
        myCard = oysterCardForTesting.getMyCard();
        oysterCardForTesting.connectCardReaders();
    }

    public void JourneyTouchAssertion() {

        oysterCardForTesting.getStartStationOysterReader().touch(myCard);
        List<JourneyEvent> eventLog = oysterCardForTesting.getCardReadersData().getEventLog();

        JourneyEvent expectedJourneyStart = new JourneyStart(myCard.id(),oysterCardForTesting.getStartStationOysterReader().id());
        Assert.assertTrue(eventLog.contains(expectedJourneyStart));

        Set<UUID> currentlyTravelling = oysterCardForTesting.getCardReadersData().getCurrentlyTravelling();

        Assert.assertTrue(currentlyTravelling.contains(myCard.id()));

        oysterCardForTesting.getEndStationOysterReader().touch(myCard);
        JourneyEvent expectedJourneyEnd = new JourneyEnd(myCard.id(),oysterCardForTesting.getEndStationOysterReader().id());

        Assert.assertTrue(eventLog.get(eventLog.size()).equals(expectedJourneyEnd));
    }
}
