package tests;

import org.junit.Test;

public class OysterCardTests {

    private OysterScanPageObject oysterScanPageObject;

    public OysterCardTests(){
        oysterScanPageObject = new OysterScanPageObject();
    }

    @Test
    public void checkCardTouches() {

        oysterScanPageObject.createOysterCardAndRegisterCardReaders();
        oysterScanPageObject.JourneyTouchAssertion();
    }


}
