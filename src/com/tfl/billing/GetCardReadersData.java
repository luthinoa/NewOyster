package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.oyster.ScanListener;
import com.tfl.external.CustomerDatabase;

import java.util.*;

public class GetCardReadersData implements ScanListener {

    /*
    This class is in charge of connecting the card readers, and getting data from them by creating the eventLog list,
    which is updated after every touch card .
     */

    private Set<UUID> currentlyTravelling;
    private List<JourneyEvent> eventLog;

    public GetCardReadersData() {
        this.currentlyTravelling = new HashSet<>();
        eventLog = new ArrayList<>();
    }

    /*
    connect card readers
     */
    public void connect(OysterCardReader...oysterCardReaders) {
        for (OysterCardReader cardReader : oysterCardReaders) {

            //for each card reader register it (register will add the scanListener instant to the list of listeners in oysterCardReader).
            cardReader.register(this);
        }
    }

    /*
    This method implements cardScanned from ScanListener.
    every time a cardReader calls "touch" with a specific card,
    This method is called, and adds a new JourneyEvent (start/end) to the eventLog list.
     */
    @Override
    public void cardScanned(UUID cardId, UUID readerId) {

            /*
            if the current card exists in the currentlyTravelling, then the "touch" is translated as the end of the Journey,
            we remove the cardID from the currentlyTravelling since it's the end of this journey.
            a new JourneyEvent is added to the eventLog, and is set to false (since it's NOT the start).
            */

        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEvent(cardId, readerId,false));
            currentlyTravelling.remove(cardId);

            /*
            if the current card not exists in the currentlyTravelling, then the "touch" is translated as the start of the Journey,
            we add the cardID to the currentlyTravelling since it's the start of this journey.
            a new JourneyEvent is added to the eventLog, and is set to true (since it's the start).
            */

        } else {
            if (CustomerDatabase.getInstance().isRegisteredId(cardId)) {
                currentlyTravelling.add(cardId);
                eventLog.add(new JourneyEvent(cardId, readerId,true));
            } else {
                throw new UnknownOysterCardException(cardId);
            }
        }
    }

    public List<JourneyEvent> getEventLog() {return eventLog;}

    public Set<UUID> getCurrentlyTravelling() {return currentlyTravelling;}
}
