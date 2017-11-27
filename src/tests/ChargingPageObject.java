package tests;

import com.oyster.OysterCard;
import com.tfl.billing.TravelTracker;
import org.junit.Assert;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Calendar;


public class ChargingPageObject {

    static final BigDecimal LONG_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.70);
    static final BigDecimal SHORT_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(1.60);
    static final BigDecimal LONG_PEAK_JOURNEY_PRICE = new BigDecimal(3.80);
    static final BigDecimal SHORT_PEAK_JOURNEY_PRICE = new BigDecimal(2.90);
    private TravelTracker travelTracker;
    private OysterCardForTesting oysterCardForTesting;
    private OysterCard oysterCard;


    public ChargingPageObject()
    {
        oysterCardForTesting = new OysterCardForTesting();
    }

    public void createJourney() throws InvocationTargetException, IllegalAccessException {

        oysterCardForTesting.connectCardReaders();
        travelTracker = new TravelTracker(oysterCardForTesting.getCardReadersData());

        oysterCard = oysterCardForTesting.getMyCard();
        oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
        oysterCardForTesting.getEndStationOysterReader().touch(oysterCard);
    }

    public void assertCharge(boolean longJourney,boolean peak) throws InvocationTargetException, IllegalAccessException {

        travelTracker.chargeAccounts();
        BigDecimal cost = travelTracker.getCost();
        if (peak) {

            if (longJourney) {
                Assert.assertTrue(cost.doubleValue()==(LONG_PEAK_JOURNEY_PRICE).doubleValue());
            }

            else {
                Assert.assertTrue(cost.doubleValue()==(SHORT_PEAK_JOURNEY_PRICE).doubleValue());
            }
        }

        else {

            if(longJourney) {
                Assert.assertTrue(cost.doubleValue()==(LONG_OFF_PEAK_JOURNEY_PRICE).doubleValue());
            }

            else {
                Assert.assertTrue(cost.doubleValue()==(SHORT_OFF_PEAK_JOURNEY_PRICE).doubleValue());
            }
        }
    }

    public void createTypeOfJourney(boolean longJourney, boolean peak) throws InvocationTargetException, IllegalAccessException {

        int hourOfDay;
        int minutesOfStart = 10;
        int minutesOfEnd;

        if (peak) {

            hourOfDay = 18;

            if (longJourney) {
                minutesOfEnd = 50;
            }

            else {
                minutesOfEnd = 15;
            }
        }

        else {

            hourOfDay = 11;
            if(longJourney) {
                minutesOfEnd = 50;
            }

            else {
                minutesOfEnd = 15;
            }
        }

        Calendar calendar = Calendar.getInstance();


        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minutesOfStart);

        int eventLogSize = oysterCardForTesting.getCardReadersData().getEventLog().size();

        if(eventLogSize<2) {
            eventLogSize = 2;
        }

        oysterCardForTesting.getCardReadersData().getEventLog().get(eventLogSize-2).setTime(calendar.getTimeInMillis());

        calendar.set(Calendar.MINUTE,minutesOfEnd);

        oysterCardForTesting.getCardReadersData().getEventLog().get(eventLogSize-1).setTime(calendar.getTimeInMillis());
    }

    public void createJourneysToPassLowerCapValue() throws InvocationTargetException, IllegalAccessException {

        for (int i=0;i<4;i++) {
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            oysterCardForTesting.getStartStationOysterReader().touch(oysterCard);
            createTypeOfJourney(true,false);
        }
    }

    public void assertLowerCap() throws InvocationTargetException, IllegalAccessException {

        travelTracker.chargeAccounts();
        BigDecimal cost = travelTracker.getCost();

        Assert.assertTrue(cost.doubleValue()==BigDecimal.valueOf(7.0).doubleValue());
    }






}
