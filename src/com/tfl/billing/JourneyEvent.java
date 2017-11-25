package com.tfl.billing;

import java.util.UUID;

public abstract class JourneyEvent implements Time {

    private final UUID cardId;
    private final UUID readerId;
    private final long time;

    public JourneyEvent(UUID cardId, UUID readerId) {
        this.cardId = cardId;
        this.readerId = readerId;
        this.time = getTimeInMillies();
    }

    public UUID cardId() {
        return cardId;
    }

    public UUID readerId() {
        return readerId;
    }

    public long time() {
        return time;
    }

    @Override
    public long getTimeInMillies() {
        long timeInMillies = System.currentTimeMillis();
        return  timeInMillies;
    }
}
