package com.tfl.billing;

public class IsLong implements Characteristics {

    /*
    This class checks if the passed journey is long/short.
    long = longer than 25 minutes.
    otherwise it's short.
     */

    @Override
    public boolean isThisCharacteristicTrue(Journey journey) {

        int timeOfJourneyInSeconds = journey.durationSeconds();
        float timeOfJourney = timeOfJourneyInSeconds / 60 + timeOfJourneyInSeconds % 60;

        if (timeOfJourney>=25) {

            //if the journey is long, add the string to the journey's characteristics list.
            return true;
        }
        return false;
    }
}
