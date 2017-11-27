package com.tfl.billing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Journey {

    private final JourneyEvent start;
    private final JourneyEvent end;
    private List<String> characteristics;

    public Journey(JourneyEvent start, JourneyEvent end) {
        this.start = start;
        this.end = end;
        this.characteristics = new ArrayList<String>();
    }

    public UUID originId() {
        return start.readerId();
    }

    public UUID destinationId() {
        return end.readerId();
    }

    public String formattedStartTime() {
        return format(start.time());
    }

    public String formattedEndTime() {
        return format(end.time());
    }

    public Date startTime() {
        return new Date(start.time());
    }

    public Date endTime() {
        return new Date(end.time());
    }

    public int durationSeconds() {
        return (int) ((end.time() - start.time()) / 1000);
    }

    public String durationMinutes() {
        return "" + durationSeconds() / 60 + ":" + durationSeconds() % 60;
    }

    private String format(long time) {
        return SimpleDateFormat.getInstance().format(new Date(time));
    }

    public List<String> getCharacteristics() {return characteristics;}

    public void setCharacteristics(String characteristic) {characteristics.add(characteristic);}

    public JourneyEvent getStart() {return start;}
    public JourneyEvent getEnd() {return end;}


}
