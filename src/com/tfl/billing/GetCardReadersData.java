package com.tfl.billing;

import com.oyster.OysterCardReader;
import com.oyster.ScanListener;
import com.tfl.external.CustomerDatabase;

import java.util.*;

public class GetCardReadersData implements ScanListener {

    private Set<UUID> currentlyTravelling;
    private List<JourneyEvent> eventLog;

    public GetCardReadersData() {
        this.currentlyTravelling = new HashSet<>();
        eventLog = new ArrayList<>();
    }

    public void connect(OysterCardReader...oysterCardReaders) {
        for (OysterCardReader cardReader : oysterCardReaders) {
            cardReader.register(this);
        }
    }

    @Override
    public void cardScanned(UUID cardId, UUID readerId) {
        if (currentlyTravelling.contains(cardId)) {
            eventLog.add(new JourneyEvent(cardId, readerId,false));
            currentlyTravelling.remove(cardId);
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
