package tests;

import com.oyster.OysterCard;
import com.tfl.billing.JourneyEvent;
import com.tfl.billing.TravelTracker;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;


public class ChargingPageObject {

    /*
    This page object will test that the charging for all types of journeys (peak/off peak/long/short/caps) is calculated properly.
     */

    static final BigDecimal LONG_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.70);
    static final BigDecimal SHORT_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(1.60);
    static final BigDecimal LONG_PEAK_JOURNEY_PRICE = new BigDecimal(3.80);
    static final BigDecimal SHORT_PEAK_JOURNEY_PRICE = new BigDecimal(2.90);

    private TravelTracker travelTracker;
    private OysterCardForTesting oysterCardForTesting;
    private OysterCard oysterCard;


    public ChargingPageObject()
    {
        //holds oyster card and 2 card readers for testing.
        oysterCardForTesting = new OysterCardForTesting();
    }

    /*
    Create a journey for testing purposes.
     */
    public void createJourney(boolean isThisACapTest) throws InvocationTargetException, IllegalAccessException {

        //create card readers for testing and connect them.
        oysterCardForTesting.connectCardReaders();

        //create a travelTracker for testing purposes.
        travelTracker = new TravelTracker(oysterCardForTesting.getCardReadersData());

        //touch twice to create a start and end of journey.
        oysterCard = oysterCardForTesting.getMyCard();

        if(!isThisACapTest) {
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            oysterCardForTesting.getEndStationOysterReader().touch(oysterCard);
        }
    }

    /*
    This method will input the type of journey (longPeak/shortPeak/longOffPeak/shortOffPeak), and assert that the right charge value is calculated.
     */
    public void assertCharge(boolean longJourney,boolean peak) throws InvocationTargetException, IllegalAccessException {

        //get the cost charged from the travelTracker
        travelTracker.chargeAccounts();
        BigDecimal cost = travelTracker.getCostForTesting();

        //assert that correct charge has been calculated
        if (peak) {

            //peakLong
            if (longJourney) {
                Assert.assertTrue(cost.doubleValue()==(LONG_PEAK_JOURNEY_PRICE).doubleValue());
            }

            //peakShort
            else {
                Assert.assertTrue(cost.doubleValue()==(SHORT_PEAK_JOURNEY_PRICE).doubleValue());
            }
        }

        else {

            //offPeakLong
            if(longJourney) {
                Assert.assertTrue(cost.doubleValue()==(LONG_OFF_PEAK_JOURNEY_PRICE).doubleValue());
            }

            //offPeakShort
            else {
                Assert.assertTrue(cost.doubleValue()==(SHORT_OFF_PEAK_JOURNEY_PRICE).doubleValue());
            }
        }
    }

    /*
    This method will input the type of journey (longPeak/shortPeak/longOffPeak/shortOffPeak), change our journey to the have the time and length required for
    it to have the input journey type.
    */
    public void createTypeOfJourney(boolean longJourney, boolean peak) throws InvocationTargetException, IllegalAccessException {

        int hourOfDay;

        //minutes of start JourneyEvent is 10 min. So length of Journey would always be timeOfEndJourneyEvent - 10minutes.
        int minutesOfStart = 10;
        int minutesOfEnd;

        if (peak) {

            //of journey is peak, set the hour of the day in the journey to be 6pm (which is peak time).
            hourOfDay = 18;

            if (longJourney) {
                //Long Journey -> set minutes of the end JourneyEvent to be 50. (so length of journey would be 40 min)
                minutesOfEnd = 50;
            }

            else {
                //Short Journey -> set minutes of the end JourneyEvent to be 15. (so length of journey would be 5 min)
                minutesOfEnd = 15;
            }
        }

        else {

            //if offPeak set the hour of day to be 11am (which is offPeak time).
            hourOfDay = 11;

            if(longJourney) {
                //Long Journey -> set minutes of the end JourneyEvent to be 50. (so length of journey would be 40 min)
                minutesOfEnd = 50;
            }

            else {
                //Short Journey -> set minutes of the end JourneyEvent to be 15. (so length of journey would be 5 min)
                minutesOfEnd = 15;
            }
        }

        //get eventLog
        List<JourneyEvent> eventLog = oysterCardForTesting.getCardReadersData().getEventLog();
        int eventLogSize = eventLog.size();

        //fail the test if eventLog is empty.
        if(eventLogSize<2) {
            eventLogSize = 2;
            System.out.println("Error - Your eventLog is empty");
            Assert.assertTrue(false);
        }

        /*
        Last two events in eventLog would always be our JourneyEvents we want to test.
        The JourneyEvent in index eventLogSize-2 would be the start event, and the one in index eventLogSize-1 would be
        the end event.
         */

        //set time of start JourneyEvent (minutes and hours) according to the values determined above.
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minutesOfStart);

        JourneyEvent startJourneyEvent = eventLog.get(eventLogSize-2);
        startJourneyEvent.setTime(calendar.getTimeInMillis());

        //set time of end JourneyEvent (minutes and hours) according to the values determined above.
        calendar.set(Calendar.MINUTE,minutesOfEnd);

        JourneyEvent endJourneyEvent = eventLog.get(eventLogSize-1);
        endJourneyEvent.setTime(calendar.getTimeInMillis());
    }

    /*
    This method will create 5 journey, so that the sum of cost of all journey will pass the cap value.
    We are creating 5 long off peak journeys - so cap would be 7.
     */
    public void createJourneysToPassLowerCapValue() throws InvocationTargetException, IllegalAccessException {

        //create card readers for testing and connect them.
        oysterCardForTesting.connectCardReaders();

        //create a travelTracker for testing purposes.
        travelTracker = new TravelTracker(oysterCardForTesting.getCardReadersData());

        for (int i=0;i<4;i++) {
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            createTypeOfJourney(true,false);
        }
    }

    /*
    This method will assert that the cost value of the 5 long off peak journeys is 7.
     */
    public void assertLowerCap() throws InvocationTargetException, IllegalAccessException {

        travelTracker.chargeAccounts();
        BigDecimal cost = travelTracker.getCostForTesting();
        Assert.assertTrue(cost.doubleValue()==BigDecimal.valueOf(7.0).doubleValue());
    }

    /*
    This method will create 5 journey, so that the sum of cost of all journey will pass the cap value.
    We are creating 5 long peak journeys - so cap would be 9.
     */
    public void createJourneysToPassHigherCapValue() throws InvocationTargetException, IllegalAccessException {

        //create card readers for testing and connect them.
        oysterCardForTesting.connectCardReaders();

        //create a travelTracker for testing purposes.
        travelTracker = new TravelTracker(oysterCardForTesting.getCardReadersData());
        oysterCard = oysterCardForTesting.getMyCard();

        for (int i=0;i<4;i++) {
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            createTypeOfJourney(true,true);
        }
    }

    /*
    This method will assert that the cost value of the 5 long peak journeys is 9.
     */
    public void assertHigherCap() throws InvocationTargetException, IllegalAccessException {

        travelTracker.chargeAccounts();
        BigDecimal cost = travelTracker.getCostForTesting();
        Assert.assertTrue(cost.doubleValue()==BigDecimal.valueOf(9.0).doubleValue());
    }
}
