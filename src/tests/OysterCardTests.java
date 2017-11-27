package tests;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class OysterCardTests {

    private OysterScanPageObject oysterScanPageObject;
    private CreateJourneysPageObject createJourneysPageObject;
    private ChargingPageObject chargingPageObject;

    public OysterCardTests(){
        oysterScanPageObject = new OysterScanPageObject();
        chargingPageObject = new ChargingPageObject();
        createJourneysPageObject = new CreateJourneysPageObject();
    }
//
//    @Test
//    public void checkCardTouches() {
//        oysterScanPageObject.createOysterCardAndRegisterCardReaders();
//        oysterScanPageObject.JourneyTouchAssertion();
//    }
//
//    @Test
//    public void checkCreationOfJourneys(){
//        createJourneysPageObject.createEventLog();
//        createJourneysPageObject.assertJourneyEventsForOneCustomer();
//        createJourneysPageObject.assertJourneysForOneCustomer();
//    }
//
//    @Test
//    public void checkLongPeakJourney() throws InvocationTargetException, IllegalAccessException {
//        chargingPageObject.createJourney();
//        chargingPageObject.createTypeOfJourney(true,true);
//        chargingPageObject.assertCharge(true,true);
//    }

    @Test
    public void checkLongOffPeakJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney();
        chargingPageObject.createTypeOfJourney(true,false);
        chargingPageObject.assertCharge(true,false);
    }

//    @Test
//    public void checkShortPeakJourney() throws InvocationTargetException, IllegalAccessException {
//        chargingPageObject.createJourney();
//        chargingPageObject.createTypeOfJourney(false,true);
//        chargingPageObject.assertCharge(false,true);
//    }
//
//    @Test
//    public void checkShortOffPeakJourney() throws InvocationTargetException, IllegalAccessException {
//        chargingPageObject.createJourney();
//        chargingPageObject.createTypeOfJourney(false,false);
//        chargingPageObject.assertCharge(false,false);
//    }
//
//    @Test
//    public void checkLowCapJourney() throws InvocationTargetException, IllegalAccessException {
//        chargingPageObject.createJourney();
//        chargingPageObject.createJourneysToPassLowerCapValue();
//        chargingPageObject.assertLowerCap();
//    }
}
