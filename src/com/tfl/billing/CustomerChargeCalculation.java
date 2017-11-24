package com.tfl.billing;
import com.tfl.external.PaymentsSystem;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.lang.reflect.Method;
import java.util.*;

public class CustomerChargeCalculation {

    /*
    This class takes a list of a customers journeys and calculates the total charge of his journeys.
     */


    /*
    The following BigDecimals are set prices for the 4 different types of journeys.
     */
    static final BigDecimal LONG_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(2.70);
    static final BigDecimal SHORT_OFF_PEAK_JOURNEY_PRICE = new BigDecimal(1.60);
    static final BigDecimal LONG_PEAK_JOURNEY_PRICE = new BigDecimal(3.80);
    static final BigDecimal SHORT_PEAK_JOURNEY_PRICE = new BigDecimal(2.90);

    private BigDecimal customerTotal;

    private List<Journey> customerJourneys;

    /*
    This list holds strings which represent characteristics of a journey, which are names as the methods which check
    these characteristics of a journey. i.e - Note there are methods "isLong" and "IsPeak".
    To add characteristic for a journey, add it to the list and create a method (called the same) which checks for that
    characteristic.
     */
    private List<String> journeyCharacteristicsOptions = new ArrayList<String>(Arrays.asList("isLong","IsPeak"));
    private Method[] methods = CustomerChargeCalculation.class.getMethods();


    public CustomerChargeCalculation(List<Journey> journeys) {
        customerJourneys = journeys;
        customerTotal = new BigDecimal(0);
    }

    public void determineTypeOfJourney(Journey journey) throws InvocationTargetException, IllegalAccessException {


        CharacteristicsFactory characteristicsFactory = new CharacteristicsFactory();
        Characteristics isPeak = characteristicsFactory.getCharacteristic("IsPeak");
        Characteristics isLong = characteristicsFactory.getCharacteristic("IsLong");

        if (isPeak.isThisCharacteristicTrue(journey)==true) {
            journey.getCharacteristics().add("isPeak");
        }

        if (isLong.isThisCharacteristicTrue(journey)==true) {
            journey.getCharacteristics().add("isLong");
        }

    }

    /*
    This method iterates through the customer's journeys, and according to the journey's characteristic list (which
    was generated in the above methods), it sums the different charge rates of the journeys to a final charging value.
     */
    public BigDecimal chargeJourneys() throws InvocationTargetException, IllegalAccessException {


        for(Journey journey:customerJourneys) {

            BigDecimal charge = BigDecimal.valueOf(0);

            //triggering the generation of a characteristic list for the journey.
            determineTypeOfJourney(journey);

            List<String> characteristics = journey.getCharacteristics();

            //if the list of the journey's characteristic is empty, it is not peak/long.
            if (characteristics.size() == 0) {
                charge = SHORT_OFF_PEAK_JOURNEY_PRICE;
            }

            //if the list has one element, it's either long or peak.
            if (characteristics.size() == 1) {

                if (characteristics.contains("isLong")) {
                    charge = LONG_OFF_PEAK_JOURNEY_PRICE;
                }

                else {
                    charge = SHORT_PEAK_JOURNEY_PRICE;
                }
            }

            //if the list's size is 2, it's both peak and long.
            else if (characteristics.size()==2){
                charge = LONG_PEAK_JOURNEY_PRICE;
            }

            customerTotal = customerTotal.add(charge);
        }

        //determine cap value for current customer, and if its total charge exceeds the cap. Change total charge accordingly.
        determineCap();
        determineChargeAfterCap();
        customerTotal = roundToNearestPenny(customerTotal);
        return customerTotal;
}

    /*
       Determine what's the cap for the current customer.
       Default cap is 7. But if at least one of the journeys is peak, the cap would be 9.
    */

    public int determineCap() {

        int cap = 7;

        for (Journey journey : customerJourneys) {

            if (journey.getCharacteristics().contains("IsPeak")) {
                cap = 9;
                return cap;
            }
        }
        return  cap;
    }

    /*
       This method will change the charge value of the customer if it exceeds its cap.
     */
    public void determineChargeAfterCap() {

        int cap = determineCap();

        BigDecimal peakCapInBigDecimal = BigDecimal.valueOf(9.0);
        BigDecimal offPeakCapInBigDecimal = BigDecimal.valueOf(7.0);

        if ((cap==9) && (customerTotal.compareTo(peakCapInBigDecimal)>0)) {
            customerTotal = peakCapInBigDecimal;
        }

        else if (customerTotal.compareTo(offPeakCapInBigDecimal)>0) {
            customerTotal = offPeakCapInBigDecimal;
        }
    }

    private BigDecimal roundToNearestPenny(BigDecimal poundsAndPence) {
        return poundsAndPence.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
