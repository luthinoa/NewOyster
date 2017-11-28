package com.tfl.billing;

import java.util.Calendar;
import java.util.Date;

public class IsPeak implements Characteristics {

    /*
    This class checks if the journey checks if a journey is peak/off peak.
    peak = hour of the day is between 6 to 9am, or 17 to 19pm.
    otherwise off peak.
     */

    @Override
    public boolean isThisCharacteristicTrue(Journey journey) {

        if(peak(journey)) {

            //if the journey is peak, add the string to the journey's characteristics list.
            return true;
        }

        return false;
    }

    private boolean peak(Journey journey) {
        return peak(journey.startTime()) || peak(journey.endTime());
    }

    private boolean peak(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= 6 && hour <= 9) || (hour >= 17 && hour <= 19);
    }
}
