package com.tfl.billing;

import java.util.UUID;

public class JourneyEvent{

    private final UUID cardId;
    private final UUID readerId;
    private boolean start;
    private long time;

    public JourneyEvent(UUID cardId, UUID readerId, boolean start) {
        this.cardId = cardId;
        this.readerId = readerId;
        this.time = getTimeInMillies();
        this.start = start;
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

    public void setTime(long time) {this.time = time;}

    public long getTimeInMillies() {
        long timeInMillies = System.currentTimeMillis();
        return  timeInMillies;
    }

    public boolean isStart() {return start;}

    public void setType(boolean start){this.start=start;}
}
