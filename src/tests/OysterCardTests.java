package tests;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

public class OysterCardTests {

    /*
    This test holds all unit tests for NewOyster program.
    */

    private ScanPageObject oysterScanPageObject;
    private CreateJourneysPageObject createJourneysPageObject;
    private ChargingPageObject chargingPageObject;

    public OysterCardTests() {
        oysterScanPageObject = new ScanPageObject();
        chargingPageObject = new ChargingPageObject();
        createJourneysPageObject = new CreateJourneysPageObject();
    }

    /*
    Assert that after every touch of card reader, the right type of journeyEvent (start/end) is added to the eventLog.
    Also asserts that the during a journey, cardID is held in the currentlyTravelling Set, and removed at end of journey.
     */
    @Test
    public void checkCardTouches() {
        oysterScanPageObject.createOysterCardAndRegisterCardReaders();
        oysterScanPageObject.JourneyTouchAssertion();
    }

    /*
    Asserts every step from creating a journeyEvent list for individual customer (from the total eventLog),
    then asserts creating a journey list for a customer (from the individual journeyEventList),
     */
    @Test
    public void checkCreationOfJourneys() {
        createJourneysPageObject.createEventLog();
        createJourneysPageObject.assertJourneyEventsForOneCustomer();
        createJourneysPageObject.assertJourneysForOneCustomer();
    }

    /*
    Asserts the charging of a long peak journey.
     */
    @Test
    public void checkLongPeakJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney(false);
        chargingPageObject.createTypeOfJourney(true, true);
        chargingPageObject.assertCharge(true, true);
    }

    /*
    Asserts the charging of a long off peak journey.
     */
    @Test
    public void checkLongOffPeakJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney(false);
        chargingPageObject.createTypeOfJourney(true,false);
        chargingPageObject.assertCharge(true,false);
    }

    /*
    Asserts the charging of a short peak journey.
     */
    @Test
    public void checkShortPeakJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney(false);
        chargingPageObject.createTypeOfJourney(false,true);
        chargingPageObject.assertCharge(false,true);
    }

    /*
    Asserts the charging of a short off peak journey.
     */
    @Test
    public void checkShortOffPeakJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney(false);
        chargingPageObject.createTypeOfJourney(false,false);
        chargingPageObject.assertCharge(false,false);
    }

    /*
    Asserts that lower cap is determined properly, and that if a customer passes the lower cap,
    charging value is correct.
     */
    @Test
    public void checkLowCapJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney(true);
        chargingPageObject.createJourneysToPassLowerCapValue();
        chargingPageObject.assertLowerCap();
    }

    /*
    Asserts that higher cap is determined properly, and that if a customer passes the higher cap,
    charging value is correct.
    */
    @Test
    public void checkHighCapJourney() throws InvocationTargetException, IllegalAccessException {
        chargingPageObject.createJourney(true);
        chargingPageObject.createJourneysToPassHigherCapValue();
        chargingPageObject.assertHigherCap();
    }
}
